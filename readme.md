## lonni-im
基于 `Netty`开发的 `im`项目;



## 使用教程
- 新建项目,导入 依赖
```xml
<dependencies>
    <dependency>
        <groupId>com.lonni</groupId>
        <artifactId>lonni-im-core</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.lonni</groupId>
        <artifactId>lonni-im-server</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>${spring-boot.version}</version>
    </dependency>
</dependencies> 
```

- 建立自己的 `handler` 包 ,添加处理 `handler`,如:
```java
@Component
@Slf4j
@MsgHandler(order = 1)
@ChannelHandler.Sharable
public class Login1Handler extends SimpleChannelInboundHandler<LoginRequestAction> {



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestAction msg) throws Exception {
      log.info("Login1 登录处理器接受到请求:{}",msg.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Login1 进入channelActive 方法");
    }
}

```

> 自定义的handler必须标识 `@MsgHandler` 注解,可设置处理器的添加顺序;


- 配置文件
```yaml

im:
  server:
    unionServer: false  # 是否开启tcp和ws联合端口
    tcpIp: 127.0.0.1  # tcp和联合 ip地址
    tcpPort: 9898 # tcp和联合 端口
    enableTcp: false  #是否开启tcp
    enableWs: true  # 是否开启 ws
    wsIp: 127.0.0.1 # ws ip
    wsPort: 9898 # ws 端口
    wsPrefix: /ws  # ws前缀  默认 ws 可不配置 
 
```
> 如果 `unionServer=false`,那么  `tcp`和 `ws` 只能启动一个;

- 启动
```java

@Component
public class NettyLister implements ApplicationRunner {

    @Autowired
    private ImServerBoot serverBoot;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        serverBoot.startServer();


    }
}
```













## 自定义

### 增加自定义消息类型
系统内置了某些消息,如果消息类型不够用,支持自定义消息体;

- 建立消息体,继承  `com.lonni.im.core.action.Action` 基类并重写其中方法
- 使用 `ActionFactory` 的 `addAction`方法 添加自定义的消息类型;
> 消息解码序列化的时候通过  ActionFactory 拿到具体的消息类型去解码,如果不配置会解码失败;


### 增加自定义处理事件

如果开启了 **开启事件分发** 功能,那么接受到消息会通过 `EventBus` **根据消息的类型** 拿到对应的时间处理器执行;

- 新建事件,继承 `IEvent<T,R>` 接口, 其中 T 代表接受到的消息类型,R代表返回的消息类型
- 使用 `EventBus` 添加事件,**注意:** 类型值必须和消息体的类型值一样 ;
```java
package com.lonni.im.server.demo.event;

import cn.hutool.core.lang.Singleton;
import com.lonni.im.core.action.ActionEnum;
import com.lonni.im.core.action.LoginRequestAction;
import com.lonni.im.core.action.LoginResponseAction;
import com.lonni.im.core.event.IEvent;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * LoginEvent：
 * Description:
 *
 * @author: Lonni
 * @date: 2023/2/23 0023 17:54
 */
@Slf4j
public class LoginEvent implements IEvent<LoginRequestAction, LoginResponseAction> {

    public static LoginEvent getInstance() {
        return Singleton.get(LoginEvent.class);
    }

    @Override
    public LoginResponseAction execute(LoginRequestAction requestAction, ChannelHandlerContext ctx) throws InterruptedException {
        log.info("开始处理LoginEvent");
        log.info("处理成功:{}", requestAction.toString());
        Thread.sleep(1000);
        LoginResponseAction responseAction = new LoginResponseAction(true, "denglu wanc ");
        responseAction.setMessageType(ActionEnum.LOGIN_RES.getCode());
        responseAction.setSeriaType(requestAction.getSeriaType());
        return responseAction;
    }
}





 /**
     * 程序启动成功,向eventBus中注册事件
     * @param applicationStartedEvent
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        EventBus.getInstance().putEvent(ActionEnum.LOGIN_REQ.getCode(), LoginEvent.getInstance());


    }

```




### 去掉联合端口
- 在 `ImServerBoot`  的 `startServer` 方法注释联合端口代码;
- 在 `ImConfig` 中 修改`bean` 的表达式 
```java
// 开启联合端口 配置如下 


@Bean(name = "imTcpServer",destroyMethod = "destroy")
@ConditionalOnExpression(value = "#{ 'true'.equals(environment.getProperty('im.server.unionServer'))  || ('true').equals(environment.getProperty('im.server.enableTcp')) }")
public ImTcpServer imTcpServer(ImServerProperties properties, ImTcpServerInitializer serverInitializer) {
        return  new ImTcpServer(properties,serverInitializer);
        }

@Bean(name = "imWsServer",destroyMethod = "destroy")
@ConditionalOnExpression(value = "#{ 'false'.equals(environment.getProperty('im.server.unionServer'))  &&  ('true').equals(environment.getProperty('im.server.enableWs')) }")
public ImWsServer imWsServer(ImServerProperties properties, ImWsServerInitializer serverInitializer) {
        return  new ImWsServer(properties,serverInitializer);
        }
        
// 关闭联合端口配置如下


@Bean(name = "imTcpServer",destroyMethod = "destroy")
@ConditionalOnExpression(value = "#{  ('true').equals(environment.getProperty('im.server.enableTcp')) }")
public ImTcpServer imTcpServer(ImServerProperties properties, ImTcpServerInitializer serverInitializer) {
        return  new ImTcpServer(properties,serverInitializer);
        }

@Bean(name = "imWsServer",destroyMethod = "destroy")
@ConditionalOnExpression(value = "#{  ('true').equals(environment.getProperty('im.server.enableWs')) }")
public ImWsServer imWsServer(ImServerProperties properties, ImWsServerInitializer serverInitializer) {
        return  new ImWsServer(properties,serverInitializer);
        }

        
```


### 关闭自定义handler ,开启事件分发

在 使用 `MsgHandlerUtil.addMsgHandlerToPipeline(pipeline)` 的地方将此方法注释调, 然后添加 `pipeline.addLast(MessageHandler.getInstance());`
`MessageHandler` 在 read方法中接受数据,根据数据的类型拿到事件执行;





## 记录 
### fireChannelActive
触发下一个ChannelInboundHandler的ChannelActive()方法
**注意:** 下一个处理器重写ChannelActive方法;动态添加协议就是结合 `fireChannelActive` 和`fireChannelRead` 方法

### fireChannelInActive 
触发下一个ChannelInboundHandler的ChannelInActive()方法
### fireChannelRead 
触发下一个ChannelInboundHandler的ChannelRead()方法
### fireChannelReadComplete
触发下一个ChannelInboundHandler的ChannelReadComplete ()方法






## 常见问题

- at io.netty.buffer.AbstractReferenceCountedByteBuf.release    
 当使用 `SimpleChannelInboundHandler`时重写的 `channelRead0`方法会被 `channelRead`调用,调用完之后
  **它会自动进行一次释放(即引用计数减1)**
```java
  @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean release = true;
        try {
            if (acceptInboundMessage(msg)) {
                @SuppressWarnings("unchecked")
                I imsg = (I) msg;
                channelRead0(ctx, imsg);
            } else {
                release = false;
                ctx.fireChannelRead(msg);
            }
        } finally {
            if (autoRelease && release) {
                ReferenceCountUtil.release(msg);
            }
        }
    }
```
> 解决 :
> 1:可以使用 `Unpooled.copiedBuffer`手动创建一份传入;
> 2:不想手动创建数据的话,调用 `retain()`方法 ,如:  ` ctx.fireChannelRead(((BinaryWebSocketFrame) msg).retain());`


- 错误 io.netty.channel.ChannelPipelineException: ***Handler is not a @Sharable handler...   
  错误信息的意思是自定义的 Handler 不是 @Sharable ，所以不能多次添加或删除。
> 解决: 增加@ChannelHandler.Sharable 注解

- 接上一个问题,增加可共享注解后提示 `hannelHandler is not a sharable Handler`  
> 解决: 直接使用new 方式创建 ,也不加共享注解 即可

- 注意: `ByteToMessageDecoder` 是不能共享的;如果确保是线程安全的话,可以使用 `MessageToMessageCodec` 替换;

## 注意

### 联合端口注意 
开启tcp和ws联合后动态添加是会自动调用channelRead方法,ByteToMessageDecoder检测当前处理器被删除了,会自动调用ctx.  fireChannelRead方法;
需要在实践中设置调用 channelRead还是 channelActive方法;

### 耗时操作
- [Netty案例(二)之耗时任务的处理](https://www.codeleading.com/article/6327404841/) 
- [Netty耗时的业务逻辑应该写在哪儿，有什么注意事项？](https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247484495&idx=1&sn=2931a73be8ff2207aaf9b13af4c9117f&chksm=fbc0924fccb71b594a053ab17245576aeae5915a584406ae253f09d9ff4fc3738f89328b5c22#rd)

### Netty内存溢出 

**1:入站方向:**
 'ChannelInboundHandlerAdapter' 处理器不会释放入站消息,需手动调用 `release()` 方法释放,否则最终所有handler都不释放消息导致 **内存溢出** ;   
  `SimpleChannelInboundHandler` 该类**会保证消息最终被** `release()` 掉;

**2:出站方向:**
   出站消息是有`Netty` 程序建立的,当我们调用 `write()` 或 `writeAndFlush()` 方法后, `Netty`并不会**写入到 `sokcet`上**,而是会**先写入到 `ChannelOutboundBuffer`中** ,
   
 **2.1:启用autoRead机制**
  **当channel不可写时，关闭autoRead；**

```java
public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    if (!ctx.channel().isWritable()) {
        Channel channel = ctx.channel();
        ChannelInfo channelInfo = ChannelManager.CHANNEL_CHANNELINFO.get(channel);
        String clientId = "";
        if (channelInfo != null) {
            clientId = channelInfo.getClientId();
        }

        LOGGER.info("channel is unwritable, turn off autoread, clientId:{}", clientId);
        channel.config().setAutoRead(false);
    }
}
```
**当数据可写时开启autoRead；**

```java
@Override
public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception
{
    Channel channel = ctx.channel();
    ChannelInfo channelInfo = ChannelManager.CHANNEL_CHANNELINFO.get(channel);
    String clientId = "";
    if (channelInfo != null) {
        clientId = channelInfo.getClientId();
    }
    if (channel.isWritable()) {
        LOGGER.info("channel is writable again, turn on autoread, clientId:{}", clientId);
        channel.config().setAutoRead(true);
    }
}

```
> 说明：
>  autoRead的作用是更精确的速率控制，如果打开的时候Netty就会帮我们注册读事件。当注册了读事件后，如果网络可读，则Netty就会从channel读取数据。那如果autoread> 关掉后，则Netty会不注册读事件。
> 这样即使是对端发送数据过来了也不会触发读事件，从而也不会从channel读取到数据。当recv_buffer满时，也就不会再接收数据。

**2.2: 设置高低水位**
`serverBootstrap.option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024 * 1024, 8 * 1024 * 1024));`
> 注：高低水位配合后面的isWritable使用

**2.3 增加channel.isWritable()的判断**

channel是否可用除了校验channel.isActive()还需要加上channel.isWrite()的判断，isActive只是保证连接是否激活，而是否可写由isWrite来决定。

```java
private void writeBackMessage(ChannelHandlerContext ctx, MqttMessage message) {
    Channel channel = ctx.channel();
    //增加channel.isWritable()的判断
    if (channel.isActive() && channel.isWritable()) {
        ChannelFuture cf = channel.writeAndFlush(message);
        if (cf.isDone() && cf.cause() != null) {
            LOGGER.error("channelWrite error!", cf.cause());
            ctx.close();
        }
    }
}
```
> 注：isWritable可以来控制ChannelOutboundBuffer，不让其无限制膨胀。其机制就是利用设置好的channel高低水位来进行判断。
   
   


- [Netty内存溢出案例](https://www.cnblogs.com/vivotech/p/15346786.html)
- [Netty堆外内存泄漏排查，这一篇全讲清楚了(含排查工具)](http://www.javashuo.com/article/p-kounoory-o.html)