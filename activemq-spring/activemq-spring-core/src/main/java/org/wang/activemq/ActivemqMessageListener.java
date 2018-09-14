package org.wang.activemq;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

/**
 * 
 * @title: ActivemqMessageListener.java
 * @author: wei.wang
 * @date: 2018年9月14日
 */
public class ActivemqMessageListener {

	Logger logger = LoggerFactory.getLogger(ActivemqMessageListener.class);

	@JmsListener(destination = JmsTemplateUtil.destination, containerFactory = ActivemqConfig.QUEUE_LISTENER_CONTAINERFACTORY)
	public void onQueueMessage(Message message) {
		if (message instanceof ActiveMQTextMessage) {
			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			try {
				String text = msg.getText();
				logger.info("consumeQueue:{}", text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	@JmsListener(destination = JmsTemplateUtil.destination, containerFactory = MessageDemo.TOPIC_LISTENER_CONTAINERFACTORY)
	public void onTopicMessage(Message message) {
		if (message instanceof ActiveMQTextMessage) {
			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			try {
				String text = msg.getText();
				logger.info("consumeTopic:{}", text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
