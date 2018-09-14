package org.wang.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: ConsumeTopicMessageDemo.java
 * @author: wei.wang
 */
public class ConsumeTopicMessageDemo {

	static Logger logger = LoggerFactory.getLogger(ConsumeTopicMessageDemo.class);
	private static final String clientID = ConsumeTopicMessageDemo.class.getName();
	private static final String TOPIC = "topic";
	private ConnectionFactory factory;
	private Connection connection;
	private Session session;
	// 消息目的地，点对点：Queue，订阅模式：Topic
	private Destination destination;
	// 消费者，消费消息数据
	private MessageConsumer consumer;
	// 持久订阅者
	private TopicSubscriber subscriber;
	private Topic topic;
	private Message message;

	public static void main(String[] args) throws JMSException {
		ConsumeTopicMessageDemo activemqDemo = new ConsumeTopicMessageDemo();
		activemqDemo.consumeTopicMessage();
		activemqDemo.consumeDurableTopicMessage();
		activemqDemo.recieveDurableTopicMessage();
	}

	public void consumeTopicMessage() throws JMSException {
		factory = new ActiveMQConnectionFactory();
		connection = factory.createConnection();
		connection.setClientID(clientID + "_topic");
		connection.start();// 消费端必须启动connection
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic(TOPIC);
		consumer = session.createConsumer(destination);
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
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
		});
		// consumer.close();
		// session.close();
		// connection.close();
	}

	public void consumeDurableTopicMessage() throws JMSException {
		factory = new ActiveMQConnectionFactory();
		connection = factory.createConnection();
		connection.setClientID(clientID + "_consumeDurableTopic");
		connection.start();// 消费端必须启动connection
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = new ActiveMQTopic(TOPIC);
		subscriber = session.createDurableSubscriber(topic, TOPIC);
		subscriber.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				if (message instanceof ActiveMQTextMessage) {
					ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
					try {
						String text = msg.getText();
						logger.info("consumeDurableTopic:{}", text);
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

	public void recieveDurableTopicMessage() throws JMSException {
		factory = new ActiveMQConnectionFactory();
		connection = factory.createConnection();
		connection.setClientID(clientID + "_recieveDurableTopic");
		connection.start();// 消费端必须启动connection
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = new ActiveMQTopic(TOPIC);
		subscriber = session.createDurableSubscriber(topic, TOPIC);
		message = subscriber.receive();
		if (message instanceof ActiveMQTextMessage) {
			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			try {
				String text = msg.getText();
				logger.info("recieveDurableTopic:{}", text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		// subscriber.close();
		// session.close();
		// connection.close();
	}

}
