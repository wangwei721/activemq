package org.wang.activemq;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class JmsTemplateUtil {

	private static Logger logger = LoggerFactory.getLogger(MessageDemo.class);
	public static final String destination = "xml-destination";

	public static void produceQueueMessage(JmsTemplate jmsTemplate) {
		sendMessage(false, jmsTemplate);
	}

	public static void produceTopicMessage(JmsTemplate jmsTemplate) {
		sendMessage(true, jmsTemplate);
	}

	private static void sendMessage(boolean pubSubDomain, JmsTemplate jmsTemplate) {
		jmsTemplate.setPubSubDomain(pubSubDomain);
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				String msg = (pubSubDomain ? "topic:" : "queue:") + new Date().toString();
				logger.info("produce:{}", msg);
				return session.createTextMessage(msg);
			}
		});
	}

	public static void consumeQueueMessage(JmsTemplate jmsTemplate) {
		consumeMessage(false, jmsTemplate);
	}

	public static void consumeTopicMessage(JmsTemplate jmsTemplate) {
		consumeMessage(true, jmsTemplate);
	}

	private static void consumeMessage(boolean pubSubDomain, JmsTemplate jmsTemplate) {
		jmsTemplate.setPubSubDomain(pubSubDomain);
		jmsTemplate.setPubSubNoLocal(false);
		Message message = jmsTemplate.receive(destination);
		if (message instanceof ActiveMQTextMessage) {
			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			try {
				String text = msg.getText();
				logger.info("consume:{}:{}", pubSubDomain ? "topic" : "queue", text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void registMessageListenerByAnnotation(String clientId, String containerFactory,
			String topicListener, Class<?> listenerClazz, boolean subscriptionDurable) throws JMSException {
		String connFactory = "connectionFactory" + clientId;
		if (clientId == null || containerFactory == null || topicListener == null || listenerClazz == null) {
			throw new JMSException("请提交完整注册信息！");
		}
		Object bean = null;
		try {
			bean = BeanUtil.getBean(containerFactory, DefaultJmsListenerContainerFactory.class);
		} catch (Exception e) {
			logger.debug("MQ containerFactory 未注册！");
		}
		if (bean != null) {
			logger.debug("MQ containerFactory{} 已注册！", containerFactory);
			throw new JMSException("请勿重复注册！");
		}
		BeanDefinitionBuilder connectionFactory = BeanDefinitionBuilder
				.genericBeanDefinition(PooledConnectionFactory.class);
		connectionFactory.addConstructorArgReference(ActivemqConfig.AMQ_CONNECTION_FACTORY);
		BeanUtil.registBean(connFactory, connectionFactory);
		BeanDefinitionBuilder listenerContainerFactory = BeanDefinitionBuilder
				.genericBeanDefinition(DefaultJmsListenerContainerFactory.class);
		listenerContainerFactory.addPropertyReference("connectionFactory", connFactory);
		listenerContainerFactory.addPropertyValue("pubSubDomain", "true");
		listenerContainerFactory.addPropertyValue("subscriptionDurable", subscriptionDurable);
		listenerContainerFactory.addPropertyValue("clientId", clientId);
		BeanUtil.registBean(containerFactory, listenerContainerFactory);
		BeanUtil.registBean(topicListener, BeanDefinitionBuilder.genericBeanDefinition(listenerClazz));
		BeanUtil.getBean(topicListener, listenerClazz);
	}
}
