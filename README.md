# webchat


## 项目目录说明：
#1.  frontend：聊天前端（浏览器版）
    前端静态工程出自 https://github.com/Coffcer/vue-chat 感谢作者对此工程的开源@Coffcer
    前后端交互为自己实现
#2.  service：聊天服务
    以Spring Boot构建的微服务架构，使用websocket协议进行浏览器与后台服务通讯，采用RabbitMQ以支持在线消息、离线消息的发送。