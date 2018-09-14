package org.wang.activemq;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: MessageListener.java
 * @author: wei.wang
 * @date: 2018年9月13日
 */
public class MessageListener implements javax.jms.MessageListener {
	Logger logger = LoggerFactory.getLogger(MessageListener.class);

	@Override
	public void onMessage(Message message) {
		if (message instanceof ActiveMQTextMessage) {
			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			try {
				String text = msg.getText();
				logger.info("lisenterConsume:{}", text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
