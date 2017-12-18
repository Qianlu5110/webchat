package com.qianlu.server;

import com.qianlu.cache.WsSessionCache;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;

/**
 * Websocket服务
 *
 * @author Qianlu
 * @date 2017/12/07 15:42
 **/
@ServerEndpoint(value = "/websocket/{username}")
@Component
public class WebSocketServer {

    /**
     * 连接建立成功调用的方法
     *
     * @param username websocket 使用者
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        WsSessionCache.setWsSession(username, session);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param username websocket 使用者
     */
    @OnClose
    public void onClose(@PathParam("username") String username) {
        WsSessionCache.removeWsSession(username);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param username websocket 使用者
     * @param message  客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(@PathParam("username") String username, String message) {
        //群发消息
        for (String usernameKey : WsSessionCache.getWebSocketSessionMap().keySet()) {
            try {
                //群发消息不给自己发送
                if (!Objects.equals(username, usernameKey)) {
                    WsSessionCache.getWebSocketSessionMap().get(usernameKey).getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

}
