package com.javapai.practice.rabbitmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

/**
 * 基本概念： <br>
 * <strong>Virtual Host(虚拟主机)：</strong> <br>
 * vhost是用户权限控制的基本粒度，vhost是为了组织exchanges, queues, and bindings提出的概念。<br>
 * 用户只能访问与之绑定的vhost且不同的vhost之间是隔离的。 <br>
 * <br>
 * <strong>Queue(队列): <br>
 * <br>
 * <strong>exchange(交换机): <br>
 * <br>
 * <strong>topic:<br>
 * <strong>Message(消息): <br>
 * <br>
 * 最后，应用AMQP开放端口为5672,后台adminUI开放端口为15672。
 * 参考：http://www.cnblogs.com/me-sa/archive/2012/10/20/RabbitMQ_VHost_Exchanges_queues_bindings_and_Channels.html
 * @author liuxiang
 *
 */
public class TestRabbitMQ {
	/* 默认队列名称 */
	static String DEFAULT_QUEUE_NAME="queue_test";
	
//	public static void main(String[] args) throws IOException, TimeoutException {
//		/* 消费者 */
//		QueueConsumer consumer = new QueueConsumer("queueY");
//		Thread consumerThread = new Thread(consumer);
//		consumerThread.start();
//
//		/* 生产者 */
//		QueueProducer producer = new QueueProducer("queueX");
//		for (int i = 0; i < 10000; i++) {
//			HashMap<String,Integer> message = new HashMap<String,Integer>();
//			message.put("message number", i);
//			producer.sendMessageToQueue(message);
//			System.out.println("Message Number " + i + " sent.");
//		}
//	}
	
	@Test
	public void testQueneProducer() throws IOException, TimeoutException {
		/* 生产者 */
		QueueProducer producer = new QueueProducer(DEFAULT_QUEUE_NAME);
		for (int i = 0; i < 1000; i++) {
			HashMap<String,Integer> message = new HashMap<String,Integer>();
			message.put("message number", i);
			producer.sendMessageToQueue(message);
			System.out.println("Message Number " + i + " sent.");
		}
	}
	
	@Test
	public void testQueueConsumer() throws IOException, TimeoutException {
		/* 消费者 ,client有实现：QueueingConsumer */
		QueueConsumer consumer = new QueueConsumer(DEFAULT_QUEUE_NAME);
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();
	}

}
