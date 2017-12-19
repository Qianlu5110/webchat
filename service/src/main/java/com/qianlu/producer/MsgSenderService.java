package com.qianlu.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlu.conf.MqConfig;
import com.qianlu.pojo.MessageDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息发送业务层
 *
 * @author Qianlu
 * @date 2017/12/12 11:23
 **/
@Component
public class MsgSenderService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @param messageDTO 消息体
     */
    public void send(MessageDTO messageDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.rabbitTemplate.convertAndSend(MqConfig.ONLINE_MESSAGE_QUEUE_NAME, mapper.writeValueAsString(messageDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
