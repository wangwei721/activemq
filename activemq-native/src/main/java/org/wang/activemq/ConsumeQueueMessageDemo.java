package org.wang.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: ConsumeQueueMessageDemo.java
 * @author: wei.wang
 */
public class ConsumeQueueMessageDemo {

	static Logger logger = LoggerFactory.getLogger(ConsumeQueueMessageDemo.class);
	private static final String clientID = ConsumeQueueMessageDemo.class.getName();
	private static final String QUEUE = "queue";
	private ConnectionFactory factory;
	private Connection connection;
	private Session session;
	// 消息目的地，点对点：Queue，订阅模式：Topic
	private Destination destination;
	// 消费者，消费消息数据
	private MessageConsumer consumer;
	private Message message;

	public static void main(String[] args) throws JMSException {
		ConsumeQueueMessageDemo activemqDemo = new ConsumeQueueMessageDemo();
		activemqDemo.consumeQueueMessage();
		activemqDemo.recieveQueueMessage();
	}

	public void consumeQueueMessage() throws JMSException {
		factory = new ActiveMQConnectionFactory();
		connection = factory.createConnection();
		connection.setClientID(clientID + "_consumeQueue");
		connection.start();// 消费端必须启动connection
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue(QUEUE);
		consumer = session.createConsumer(destination);
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
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
		});
		// consumer.close();
		// session.close();
		// connection.close();
	}

	public void recieveQueueMessage() throws JMSException {
		factory = new ActiveMQConnectionFactory();
		connection = factory.createConnection();
		connection.setClientID(clientID + "_recieveQueue");
		connection.start();// 消费端必须启动connection
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue(QUEUE);
		consumer = session.createConsumer(destination);
		message = consumer.receive();
		if (message instanceof ActiveMQTextMessage) {
			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			try {
				String text = msg.getText();
				logger.info("recieveQueue:{}", text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		// consumer.close();
		// session.close();
		// connection.close();
	}

}
