


**如果要自定义编解码,请继承WsServerAioHandler 重写其中方法**

## 链接流程  
 当客户端链接后,流程如下  
- 先执行 ServerAioListenerImpl
- 在执行 ShowcaseWsMsgHandler 握手处理器 


```text
2023-02-22 11:24:23.297  INFO 6756 --- [    tio-group-3] c.l.tio.handler.ServerAioListenerImpl    : onAfterConnected
server:0.0.0.0:9326, client:127.0.0.1:55046
2023-02-22 11:24:23.305  INFO 6756 --- [    tio-group-4] c.l.tio.handler.ServerAioListenerImpl    : onAfterReceivedBytes
server:0.0.0.0:9326, client:127.0.0.1:55046
2023-02-22 11:24:23.316  INFO 6756 --- [    tio-group-4] c.l.tio.handler.ServerAioListenerImpl    : onAfterDecoded
websocket
server:0.0.0.0:9326, client:127.0.0.1:55046
2023-02-22 11:24:23.319  INFO 6756 --- [    tio-group-4] c.l.tio.handler.ShowcaseWsMsgHandler     : 收到来自127.0.0.1的ws握手包
GET /ws?name=lonni-5465 HTTP/1.1


2023-02-22 11:24:23.322  INFO 6756 --- [    tio-group-4] c.l.tio.handler.ServerAioListenerImpl    : onAfterHandled
websocket
server:0.0.0.0:9326, client:127.0.0.1:55046
2023-02-22 11:24:23.324  INFO 6756 --- [    tio-group-5] c.l.tio.handler.ServerAioListenerImpl    : onAfterSent
websocket
server:0.0.0.0:9326, client:127.0.0.1:55046
2023-02-22 11:24:23.326  INFO 6756 --- [    tio-group-6] c.l.tio.handler.ServerAioListenerImpl    : onAfterSent
websocket
server:0.0.0.0:9326, client:127.0.0.1:55046

```