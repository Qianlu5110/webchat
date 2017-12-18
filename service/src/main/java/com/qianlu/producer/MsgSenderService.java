package com.qianlu.producer;

import com.qianlu.conf.MqConfig;
import com.qianlu.pojo.IMessage;
import com.qianlu.pojo.MessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * @param username 接收人
     * @param headImg  头像
     * @param clickUrl 消息体点击URL
     * @param title    标题
     * @param content  内容
     */
    public void send(String username, String headImg, String clickUrl, String title, String content) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType(IMessage.TYPE_HTML);
        messageDTO.setUsername(username);
        messageDTO.setHeadImg(headImg);
        messageDTO.setClickUrl(clickUrl);
        messageDTO.setTitle(title);
        messageDTO.setContent(content);
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.rabbitTemplate.convertAndSend(MqConfig.ONLINE_MESSAGE_QUEUE_NAME, mapper.writeValueAsString(messageDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
