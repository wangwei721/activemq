package org.wang.activemq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 * @title: MessageDemo.java
 * @author: wei.wang
 * @date: 2018年9月13日
 */
public class MessageDemo {

	private static ApplicationContext ctx;

	public static void main(String[] args) {
		ctx = new ClassPathXmlApplicationContext("classpath:activemq.xml");
		JmsTemplate jmsTemplate = ctx.getBean("jmsTemplate", JmsTemplate.class);
		produceMessage(jmsTemplate);
		consumeMessage(jmsTemplate);
	}

	public static void produceMessage(JmsTemplate jmsTemplate) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					JmsTemplateUtil.produceQueueMessage(jmsTemplate);
					JmsTemplateUtil.produceTopicMessage(jmsTemplate);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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
