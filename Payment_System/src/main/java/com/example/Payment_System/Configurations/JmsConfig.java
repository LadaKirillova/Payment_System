package com.example.Payment_System.Configurations;


import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter; // Основной интерфейс
import org.springframework.jms.support.converter.MappingJackson2MessageConverter; // Реализация для JSON
import org.springframework.jms.support.converter.MessageType; // Для указания типа сообщения

@Configuration
@EnableJms
@org.springframework.context.annotation.Profile("!migration")
public class JmsConfig {

    @Value("${spring.artemis.user}")
    private String username;

    @Value("${spring.artemis.password}")
    private String password;

    @Value("${spring.artemis.broker-url:tcp://localhost:61616}")
    private String brokerUrl; // ← читаем из настроек

    @Value("${app.jms.listener.concurrency:3-3}")
    private String concurrency;

    @Bean
    public ConnectionFactory connectionFactory() {

        ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory(
                brokerUrl);
//        ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory("tcp://payment_artemis:61616");
        factory.setUser(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }


    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        jmsTemplate.setDeliveryPersistent(true);
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter); //
        factory.setConcurrency(concurrency);
        return factory;
    }
}
