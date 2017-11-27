package com.javapai.practice.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class QueueConsumer {
	public static void main(String[] argv) throws Exception {
		Channel channel = TestRabbitMQ.createChannel();
		channel.exchangeDeclare("com.sjd.test-exchange-fanout", "fanout");

	    String queueName = channel.queueDeclare().getQueue();
	    channel.queueBind(queueName, "com.sjd.test-exchange-fanout", "");
	    
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    QueueingConsumer consumer = new QueueingConsumer(channel);
	    channel.basicConsume(queueName, true, consumer);

	    while (true) {
	      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	      String message = new String(delivery.getBody());

	      System.out.println(" [x] Received '" + message + "'");   
	    }
	}


	// public static void main(String[] args) {
	//
	//
	// for (int i = 0; i < 10; i++) {
	// HashMap<String, String> message = new HashMap<String, String>();
	// message.put("mq-body", "我是(" + i + ")号消息报文体.");
	// producer.publishMessageToQueue(message);//为什么发送的时候，不用指定exchange?
	//// producer.sendMessageToQueue(default_exchange_name, message);
	// System.out.println(">>>>>>>>>>>生产者已发送报文:消息报文体(" + i + ").");
	// }
	// }

}