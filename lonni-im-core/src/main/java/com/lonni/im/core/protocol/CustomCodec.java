package com.lonni.im.core.protocol;

import cn.hutool.core.lang.Assert;
import com.lonni.im.core.action.Action;
import com.lonni.im.core.action.ActionFactory;
import com.lonni.im.core.constains.GloadConstains;
import com.lonni.im.core.handler.MagicsHandler;
import com.lonni.im.core.serializable.SerializeFactory;
import com.lonni.im.core.serializable.SerializerBase;
import com.lonni.im.core.util.SpringContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 具体的编解码实现
 * @author: Lonni
 * @date: 2023/2/17 0017 10:35
 */
@Component
@Slf4j
public class CustomCodec implements Serializable {


    /**
     * 编码方法
     * @param baseMessage 消息类型
     * @param out  接受值,最终写入的数据会存入到此
     */
    public void encode(Action baseMessage , ByteBuf out) throws IOException {
        try {
            //检查消息类型
            baseMessage.checkMsg();
            // 写入魔数 4 个字节  1234
            out.writeBytes(GloadConstains.magis);
            // 写入版本  1个字节 1
            out.writeByte(baseMessage.getVersion());
            // 写入序列化方式 1个字节 0
            out.writeByte(baseMessage.getSeriaType());
            //写入 消息类型 1个字节 消息体中获取
            out.writeByte(baseMessage.getMessageType());
            //写入请求序号  4 个字节
            out.writeInt(baseMessage.getSequenceId());
            // 无意义,为了满足2的整数倍,所以增加一个字节
            out.writeByte(0xff);
            // 将消息体序列化
            SerializerBase serializerBase = SerializeFactory.getInstance().chooseSerializer(baseMessage.getSeriaType().byteValue());
            Assert.notNull(serializerBase,"序列化失败!未找到序列化类型");
            byte[] bytes = serializerBase.serialize(baseMessage);
            // 添加消息长度  4 个字节
            out.writeInt(bytes.length);
            //消息体
            out.writeBytes(bytes);
        }catch (Exception exception){
            log.error("序列化失败:{}",exception.getMessage());
        }

    }


    /**
     * 解码器
     * @param in
     * @param list
     */
    public  void  decode(ChannelHandlerContext ctx,ByteBuf in, List<Object> list) throws IOException, ClassNotFoundException {
       //标记当前 读取指针的位置
        in.markReaderIndex();
        if (in.readableBytes() < 16) {
            // 包头长度不正确,直接返回;
            return;
        }
        //拿到魔数
        byte[] moshuBy = new byte[4];
        in.readBytes(moshuBy);
         MagicsHandler magicsHandler = SpringContext.getBean(MagicsHandler.class);
        Boolean checkMagic = magicsHandler.hand(moshuBy);
        Assert.isTrue(checkMagic, "魔数校验不通过!");
        log.info("魔数:" + moshuBy);
        //版本
        byte version = in.readByte();
        log.info("版本:" + version);
        //序列化方式
        //TODO  消息体反序列的时候使用对应的类型
        byte xlh = in.readByte();
        log.info("序列化方式:" + xlh);
        //消息类型
        byte messageType = in.readByte();
        log.info("消息类型:" + messageType);
        // 请求序号
        int seqId = in.readInt();
        log.info("请求序号:" + seqId);
        //无意义的byte
        byte tc = in.readByte();
        log.info("填充位:" + tc);
        //消息长度
        int bodyLength = in.readInt();
        log.info("消息长度:" + bodyLength);
        // 非法数据，关闭连接
        if (bodyLength < 0) {
            ctx.close();
        }
        // 解决粘包拆包问题
        // 读到的消息体长度如果小于传送过来的消息长度
        if (bodyLength > in.readableBytes()) {
            // 重置读取位置
            in.resetReaderIndex();
            return;
        }
        // 读取消息体,需要区分堆缓冲模式和直接缓冲模式
        byte[] array;
        if (in.hasArray()) {
            //堆缓冲
            ByteBuf slice = in.slice(in.readerIndex(), bodyLength);
            array = slice.array();
            in.retain();

        } else {
            //直接缓冲
            array = new byte[bodyLength];
            in.readBytes(array, 0, bodyLength);
        }
        // 反序列化 通过序列化类型拿到
        SerializerBase serializerBase = SerializeFactory.getInstance().chooseSerializer(xlh);
        Assert.notNull(serializerBase,"序列化失败!未找到序列化类型");
        Class<? extends Action> action = ActionFactory.getInstance().getAction((int) messageType);
        Assert.notNull(action,"序列化失败!未找到消息类型!");
        Action message = serializerBase.deSerialize(array, action);
        if (in.hasArray()) {
            //如果是堆缓冲 ,释放堆内存
            in.release();
        }
        //解码完成后加入到链中
        list.add(message);
    }


}



