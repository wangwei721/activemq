package org.wang.activemq;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil implements ApplicationContextAware {
	private static ApplicationContext ctx;

	public static Object getBean(String name) {
		return ctx.getBean(name);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return ctx.getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return ctx.getBean(name, requiredType);
	}

	public static <T> T getBean(Class<T> requiredType, Object... args) {
		return ctx.getBean(requiredType, args);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BeanUtil.ctx = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	public static <T> void registBean(String beanName, BeanDefinitionBuilder beanDefinitionBuilder) {
		DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
		dbf.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
	}

	public static void destoryBean(String beanName) {
		DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
		dbf.removeBeanDefinition(beanName);
	}
}
