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




## 注意事项


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
