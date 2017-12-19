package com.qianlu.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlu.cache.WsSessionCache;
import com.qianlu.pojo.MessageDTO;
import com.qianlu.producer.MsgSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Websocket服务
 *
 * @author Qianlu
 * @date 2017/12/07 15:42
 **/
@ServerEndpoint(value = "/websocket/{username}")
@Component
public class WebSocketServer {

    @Autowired
    private MsgSenderService msgSenderService;

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
        ObjectMapper mapper = new ObjectMapper();
        MessageDTO messageDTO = null;
        try {
            messageDTO = mapper.readValue(message, MessageDTO.class);
            if (messageDTO == null || messageDTO.getToUser() == null || "".equals(messageDTO.getToUser())) {
                return;
            }
            messageDTO.setFromUser(username);
            msgSenderService.send(messageDTO);
        } catch (IOException e) {
            e.printStackTrace();
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
