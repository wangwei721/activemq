package org.wang.activemq;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * @title: TopicMessageListener.java
 * @author: wei.wang
 * @date: 2018年9月13日
 */
@Component
public class TopicMessageListener extends MessageListenerAdapter {
	Logger logger = LoggerFactory.getLogger(TopicMessageListener.class);

	@JmsListener(destination = JmsTemplateUtil.destination)
	public void onMessage(Message message) {
		if (message instanceof ActiveMQTextMessage) {
			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			try {
				String text = msg.getText();
				logger.info("topicLisenterConsume:{}", text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
