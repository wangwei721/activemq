package org.wang.activemq;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @title: JmsTemplateUtil.java
 * @author: wei.wang
 * @date: 2018年9月13日
 */
public class JmsTemplateUtil {

	private static Logger logger = LoggerFactory.getLogger(MessageDemo.class);
	private static final String destination = "xml-destination";

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
}
