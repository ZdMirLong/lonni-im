//package com.lonni.tio.websocket.handler;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.lonni.tio.websocket.msg.ClientDirectivesVo;
//import com.lonni.tio.websocket.msg.MindPackage;
//import com.lonni.tio.websocket.msg.ResponsePackage;
//import jodd.util.ThreadUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.tio.core.ChannelContext;
//import org.tio.core.Tio;
//import org.tio.core.TioConfig;
//import org.tio.core.exception.AioDecodeException;
//import org.tio.core.intf.Packet;
//import org.tio.server.intf.ServerAioHandler;
//import org.tio.websocket.common.WsRequest;
//import org.tio.websocket.server.WsServerAioHandler;
//
//import java.io.Serializable;
//import java.io.UnsupportedEncodingException;
//import java.nio.ByteBuffer;
//import java.nio.CharBuffer;
//import java.nio.charset.Charset;
//import java.nio.charset.CharsetDecoder;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// *handler, 包括编码、解码、消息处理
// * @author: Lonni
// * @date: 2023/2/22 0022 10:58
// */
//public class ServerAioHandlerImpl extends WsServerAioHandler {
//    private  final Logger log= LoggerFactory.getLogger(ServerAioListenerImpl.class);
//    private static AtomicInteger counter = new AtomicInteger(0);
//
//    private Map<String, ChannelContext> channelMaps = new ConcurrentHashMap<>();
//
//    private Queue<ResponsePackage> respQueue = new LinkedBlockingQueue<>();
//
//    private Queue<ResponsePackage> heartQueue = new LinkedBlockingQueue<>();
//
//    public boolean offer2SendQueue(ResponsePackage respPacket) {
//        return respQueue.offer(respPacket);
//    }
//
//    public Queue<ResponsePackage> getRespQueue() {
//        return respQueue;
//    }
//
//    public boolean offer2HeartQueue(ResponsePackage respPacket) {
//        return heartQueue.offer(respPacket);
//    }
//
//    public Map<String, ChannelContext> getChannelMaps() {
//        return channelMaps;
//    }
//
//    /**
//     * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包
//     * 总的消息结构：消息体
//     * 消息体结构： 对象的json串的16进制字符串
//     */
//    @Override
//    public WsRequest decode(ByteBuffer buffer, int i, int i1, int i2, ChannelContext channelContext) throws AioDecodeException {
//        MindPackage imPacket = new MindPackage();
//        try {
//            List<JSONObject> msgList = new ArrayList<>();
//            Charset charset = Charset.forName("UTF-8");
//            CharsetDecoder decoder = charset.newDecoder();
//            CharBuffer charBuffer = decoder.decode(buffer);
//            String str = charBuffer.toString();
//            if (str.indexOf("{") != 0) {
//                str = str.substring(str.indexOf("{"));
//            }
//            if (str.indexOf("}{") > -1) {
//                String[] split = str.split("}");
//                List<String> list = Arrays.asList(split);
//                list.forEach(item -> {
//                    item += "}";
//                    msgList.add(JSON.parseObject(item));
//                });
//            } else {
//                msgList.add(JSON.parseObject(str));
//            }
//            log.info("收到" + msgList.size() + "条消息");
//            imPacket.setBody(msgList);
//            return imPacket;
//        } catch (Exception e) {
//            return imPacket;
//        }
//    }
//
//    /**
//     * 编码：把业务消息包编码为可以发送的ByteBuffer
//     */
//    @Override
//    public ByteBuffer encode(Packet packet, TioConfig groupContext, ChannelContext channelContext) {
//        ResponsePackage helloPacket = (ResponsePackage) packet;
//        JSONObject body = helloPacket.getBody();
//        //写入消息体
//        try {
//            return ByteBuffer.wrap(body.toJSONString().getBytes("GB2312"));
//        } catch (UnsupportedEncodingException e) {
//
//        }
//        return null;
//    }
//
////    /**
////     * 处理消息
////     */
////    @Override
////    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
////        MindPackage helloPacket = (MindPackage) packet;
////        List<JSONObject> msgList = helloPacket.getBody();
////        if (msgList!=null&&msgList.size()>0) {
////            msgList.forEach(body -> {
////                if (body != null) {
////                    log.info("收到设备上报信息 " + body);
////                    // 获取指令
////                    Integer type = body.getInteger("type");
////                    if (type != null) {
////                        channelContext.set("type",type);
////                        String phoneNum = body.getString("phoneNum");
////                        Tio.bindToken(channelContext,phoneNum);
////                        ResponsePackage respPacket = new ResponsePackage();
////                        switch (type) {
////                            // 接收下线指令
////                            case ClientDirectivesVo.END_REPORT_RESPONSE:
////                                //保存连接
////                                channelMaps.put(phoneNum, channelContext);
////                                //TODO 更改客户端状态为下线状态
////                                log.info("收到{}客户端下线通知",phoneNum);
////                                // 回执方法
////                                receiptHandler(respPacket,phoneNum,ClientDirectivesVo.END_REPORT_RESPONSE);
////                                break;
////                            case ClientDirectivesVo.HEART_BEET_REQUEST:  //接收心跳检查指令
////                                //保存连接
////                                channelMaps.put(phoneNum, channelContext);
////                                log.info("收到{}客户端心跳检查指令",phoneNum);
////                                // 回执方法
////                                receiptHandler(respPacket,phoneNum, ClientDirectivesVo.HEART_BEET_REQUEST);
////                                break;
//////                            case ClientDirectivesVo.GPS_START_REPORT_RESPONSE: //开始上报GPS指令
//////                                //保存连接
//////                                channelMaps.put(phoneNum, channelContext);
//////
//////                                PositioningDataReportVo vo =JSONObject.toJavaObject(body, PositioningDataReportVo.class);
//////
//////                                log.info("收到{}客户端上报GPS指令，上报数据：{}",phoneNum,vo);
//////                                // 回执方法
//////                                receiptHandler(respPacket,phoneNum,ClientDirectivesVo.GPS_START_REPORT_RESPONSE);
//////                                break;
//////                            case ClientDirectivesVo.DATA_DISTRIBUTION: //开始下发数据指令
//////                                //保存连接
//////                                channelMaps.put(phoneNum, channelContext);
//////                                log.info("收到{}客户端下发数据指令",phoneNum);
//////                                // 回执方法
//////                                DataDistributionReportVo data = new DataDistributionReportVo();
//////                                data.setUserId("20");
//////                                data.setPhone("147555544444");
//////                                data.setPlanId("1222222");
//////                                data.setXxxx("预留字段");
//////                                // 回复时的设备标志，必填
//////                                respPacket.setPhoneNum(phoneNum);
//////                                respPacket.setBody((JSONObject) JSON.toJSON(data));
//////                                respPacket.setType(ClientDirectivesVo.DATA_DISTRIBUTION);
//////                                offer2SendQueue(respPacket);
//////                                break;
////                        }
////                    }
////                }
////            });
////        }
////        return;
////    }
//
//    /**
//     * 回执信息方法
//     * @Author: laohuang
//     * @Date: 2022/11/24 13:53
//     */
////    public void receiptHandler(ResponsePackage respPacket,String phoneNum,Integer clientDirectives) {
////        // 回执信息
////        ResponseVo callVo = new ResponseVo();
////        callVo.setType(clientDirectives);
////        // 响应结果  1：成功 0：失败
////        callVo.setValue(1);
////        // 回复时的设备标志，必填
////        respPacket.setPhoneNum(phoneNum);
////        respPacket.setBody((JSONObject) JSON.toJSON(callVo));
////        respPacket.setType(clientDirectives);
////        offer2SendQueue(respPacket);
////
////    }
//
//    private Object locker = new Object();
//
//    public ServerAioHandlerImpl() {
//        try {
//            new Thread(() -> {
//                while (true) {
//                    try {
//                        ResponsePackage respPacket = respQueue.poll();
//                        if (respPacket != null) {
//                            synchronized (locker) {
//                                String phoneNum = respPacket.getPhoneNum();
//                                ChannelContext channelContext = channelMaps.get(phoneNum);
//                                if (channelContext != null) {
//                                    Boolean send = Tio.send(channelContext, respPacket);
//                                    String s = JSON.toJSONString(respPacket);
//                                    System.err.println("发送数据"+s);
//                                    System.err.println("数据长度"+s.getBytes().length);
//                                    log.info("下发设备指令 设备ip" + channelContext + " 设备[" + respPacket.getPhoneNum() + "]" + (send ? "成功" : "失败") + "消息:" + JSON.toJSONString(respPacket.getBody()));
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        log.error(e.getMessage());
//                    } finally {
//                        log.debug("发送队列大小：" + respQueue.size());
//                        ThreadUtil.sleep(10);
//                    }
//                }
//            }).start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 确保只有一个呼叫器响应后修改呼叫记录
//     * @param recordId 记录id
//     * @param resCallSn 响应的呼叫器sn
//     */
//    public synchronized void  updateCallRecordAndStopResponse(Long recordId, String resCallSn, String sn) {
//
//
//    }
//
//}
//
//
//
