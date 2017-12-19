package com.qianlu.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlu.cache.WsSessionCache;
import com.qianlu.conf.MqConfig;
import com.qianlu.pojo.MessageDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;

/**
 * 在线消息处理服务
 *
 * @author Qianlu
 * @date 2017/12/12 11:26
 **/
@Component
@RabbitListener(queues = MqConfig.ONLINE_MESSAGE_QUEUE_NAME)
public class OnLineMessageService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @RabbitHandler
    public void process(String messsage) {
        ObjectMapper mapper = new ObjectMapper();
        MessageDTO messageDTO = null;
        try {
            messageDTO = mapper.readValue(messsage, MessageDTO.class);
            if (messageDTO == null || messageDTO.getToUser() == null || "".equals(messageDTO.getToUser())) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Session session = WsSessionCache.getWebSocketSessionMap().get(messageDTO.getToUser());
            if (session != null) {
                session.getBasicRemote().sendText(messsage);
            } else {
                rabbitTemplate.convertAndSend(MqConfig.OFFLINE_MESSAGE_QUEUE_NAME, messsage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 根据队列名创建指定的队列
//     *
//     * @param queueName
//     * @return
//     */
//    private String createtQueue(String queueName) {
//        try {
//            mqConnectionFactory.createConnection().createChannel(false).queueDeclare(queueName, false, false, false, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            return queueName;
//        }
//    }
}
