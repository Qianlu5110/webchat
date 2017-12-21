package com.qianlu.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlu.cache.WsSessionCache;
import com.qianlu.conf.MqConfig;
import com.qianlu.pojo.IMessage;
import com.qianlu.pojo.MessageDTO;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;

/**
 * 离线消息处理服务
 *
 * @author Qianlu
 * @date 2017/12/15 10:07
 **/
@Component
public class OffLineMessageService {

    @Bean(name = "offline_queue")
    public Queue queue() {
        return new Queue(MqConfig.OFFLINE_MESSAGE_QUEUE_NAME);
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer(
            @Qualifier("mqConnectionFactory") ConnectionFactory mqConnectionFactory,
            @Qualifier("offline_queue") Queue queue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(mqConnectionFactory);
        container.setQueues(queue);
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
                byte[] body = message.getBody();

                ObjectMapper mapper = new ObjectMapper();
                MessageDTO messageDTO = null;
                try {
                    messageDTO = mapper.readValue(body, MessageDTO.class);
                    if (messageDTO == null || messageDTO.getToUser() == null || "".equals(messageDTO.getToUser())) {
                        return;
                    }

                    //离线消息超过一周未被发送 则销毁此离线消息
                    if (messageDTO.getCreateDate() != null && (System.currentTimeMillis() - messageDTO.getCreateDate().getTime()) > IMessage.SURVIVAL_TIME) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
                    }

                    Session session = WsSessionCache.getWebSocketSessionMap().get(messageDTO.getToUser());
                    if (session != null) {
                        //获取到指定用户的websocket session，则对MQ进行消息消费，确认
                        session.getBasicRemote().sendText(new String(body));
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
                    } else {
                        //无法获取指定用户的websocket session，则当前线程等待5秒后，将消息重新返还给MQ
                        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                        Thread.sleep(5000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return container;
    }
}