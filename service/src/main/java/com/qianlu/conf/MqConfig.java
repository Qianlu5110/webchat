package com.qianlu.conf;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Rabbit MQ 连接池配置
 *
 * @author Qianlu
 * @date 2017/12/14 17:16
 **/
@Configuration
public class MqConfig {

    /**
     * 在线消息队列
     */
    public static final String ONLINE_MESSAGE_QUEUE_NAME = "online_message";
    /**
     * 离线消息队列
     */
    public static final String OFFLINE_MESSAGE_QUEUE_NAME = "offline_message";


    @Bean(name = "mqConnectionFactory")
    @Primary
    public ConnectionFactory mqConnectionFactory(
            @Value("${spring.rabbitmq.host}") String host,
            @Value("${spring.rabbitmq.port}") int port,
            @Value("${spring.rabbitmq.username}") String username,
            @Value("${spring.rabbitmq.password}") String password
    ) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Configuration
    static class RabbitConfiguration {
        @Bean
        public SimpleRabbitListenerContainerFactory myFactory(
                SimpleRabbitListenerContainerFactoryConfigurer configurer,
                @Qualifier("mqConnectionFactory") ConnectionFactory connectionFactory) {
            SimpleRabbitListenerContainerFactory factory =
                    new SimpleRabbitListenerContainerFactory();
            configurer.configure(factory, connectionFactory);
            factory.setMessageConverter(new Jackson2JsonMessageConverter());
            return factory;
        }
    }
}
