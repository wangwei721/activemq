package org.wang.activemq;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @title: ProducerMessageDemo.java
 * @author: wei.wang
 */
public class ProducerMessageDemo {

	private static final String clientID = ProducerMessageDemo.class.getName();
	private static final String QUEUE = "queue";
	private static final String TOPIC = "topic";
	private ConnectionFactory factory;
	private Connection connection;
	private Session session;
	// 消息目的地，点对点：Queue，订阅模式：Topic
	private Destination destination;
	// 生产者，提供消息数据
	private MessageProducer producer;
	// 消息数据封装对象
	private Message message;

	public static void main(String[] args) throws JMSException {
		ProducerMessageDemo activemqDemo = new ProducerMessageDemo();
		activemqDemo.produceQueueMessage();
		activemqDemo.produceTopicMessage();
	}

	public void produceQueueMessage() throws JMSException {
		factory = new ActiveMQConnectionFactory();
		connection = factory.createConnection();
		connection.setClientID(clientID);
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue(QUEUE);
		producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		message = session.createTextMessage(new Date().toString());
		producer.send(message);
		producer.close();
		session.close();
		connection.close();
	}

	public void produceTopicMessage() throws JMSException {
		factory = new ActiveMQConnectionFactory();
		connection = factory.createConnection();
		connection.setClientID(clientID);
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic(TOPIC);
		producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		message = session.createTextMessage(new Date().toString());
		producer.send(message);
		producer.close();
		session.close();
		connection.close();
	}

}
