package org.wang.activemq;

import javax.jms.JMSException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 * 
 * @title: MessageDemo.java
 * @author: wei.wang
 * @date: 2018年9月14日
 */
public class MessageDemo {

	public static final String TOPIC_LISTENER_CONTAINERFACTORY = "topicListenerContainerfactory";
	public static final String QUEUE_LISTENER_CONTAINERFACTORY = "queueListenerContainerfactory";

	private static ApplicationContext ctx;

	public static void main(String[] args) {
		ctx = new ClassPathXmlApplicationContext("classpath:activemq.xml");
		JmsTemplate jmsTemplate = ctx.getBean("jmsTemplate", JmsTemplate.class);
		registMessageListener(jmsTemplate);
		produceMessage(jmsTemplate);
		// consumeMessage(jmsTemplate);
	}

	private static void registMessageListener(JmsTemplate jmsTemplate) {
		try {
			JmsTemplateUtil.registMessageListenerByAnnotation("spring_core_clientId", TOPIC_LISTENER_CONTAINERFACTORY,
					"activemqMessageListener", ActivemqMessageListener.class, true);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	public static void produceMessage(JmsTemplate jmsTemplate) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					JmsTemplateUtil.produceQueueMessage(jmsTemplate);
					JmsTemplateUtil.produceTopicMessage(jmsTemplate);
				}
			}
		}).start();
	}

	public static void consumeMessage(JmsTemplate jmsTemplate) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					JmsTemplateUtil.consumeQueueMessage(jmsTemplate);
					JmsTemplateUtil.consumeTopicMessage(jmsTemplate);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
