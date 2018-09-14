package org.wang.activemq;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 * 
 * @title: ActivemqConfig.java
 * @author: wei.wang
 * @date: 2018年9月14日
 */
@Configuration
public class ActivemqConfig {

	public static final String AMQ_CONNECTION_FACTORY = "amqConnectionFactory";
	public static final String QUEUE_LISTENER_CONTAINERFACTORY = "queueListenerContainerFactory";

	@Value("${brokerUrl}")
	private String brokerUrl;

	@Bean
	public ActiveMQConnectionFactory amqConnectionFactory() {
		return new ActiveMQConnectionFactory(brokerUrl);
	}

	@Bean
	public JmsTemplate jmsTemplate(PooledConnectionFactory queueConnectionFactory) {
		return new JmsTemplate(queueConnectionFactory);
	}

	@Bean
	public JmsListenerContainerFactory<?> queueListenerContainerFactory(ConnectionFactory queueConnectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(queueConnectionFactory);
		factory.setPubSubDomain(false);
		return factory;
	}

	@Bean
	public PooledConnectionFactory queueConnectionFactory(ActiveMQConnectionFactory amqConnectionFactory) {
		return new PooledConnectionFactory(amqConnectionFactory);
	}
}
