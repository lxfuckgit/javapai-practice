package com.javapai.practice.rabbitmq;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;

import com.rabbitmq.client.Channel;

/**
 * 
 * 功能概要：消息生产者
 * 
 * @author linbingwen
 * @since 2016年1月11日
 */
public class QueueProducer {

	public static void main(String[] argv) throws IOException, TimeoutException {
		Channel channel = TestRabbitMQ.createChannel();
		channel.exchangeDeclare("com.sjd.test-exchange-fanout", "fanout");

		String message = "xxx";

		channel.basicPublish("com.sjd.test-exchange-fanout", "", null, message.getBytes());
		System.out.println(" [x] Sent :'" + message + "'");
	}

}
