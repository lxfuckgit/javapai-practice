package com.javapai.practice.rabbitmq;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * 功能概要： EndPoint类型的队列
 * 
 * @author linbingwen
 * @since 2016年1月11日
 */
public abstract class EndPoint {

	protected Channel channel;
	protected Connection connection;
	protected String endPointName;
	
	String hostname="192.168.33.132";

	public EndPoint(String queueName) throws IOException, TimeoutException {
		this.endPointName = queueName;

		/* 1:Create a connection factory */
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(hostname);
		factory.setVirtualHost("/");//默认就有一个名为"/"
		factory.setPort(5672);
		factory.setUsername("admin");
		factory.setPassword("admin");
		
		/* 2:Getting a connection */
		connection = factory.newConnection();

		/* 3：Creating a channel */ 
		channel = connection.createChannel();
//        int prefetchCount = 1;
//        channel.basicQos(prefetchCount); //保证公平分发
        
//        channel.confirmSelect(); //Enables publisher acknowledgements on this channel
//        channel.addConfirmListener(new com.rabbitmq.client.ConfirmListener() {
//
//            @Override
//            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
//                System.out.println("[handleNack] :" + deliveryTag + "," + multiple);
//
//            }
//
//            @Override
//            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
//                System.out.println("[handleAck] :" + deliveryTag + "," + multiple);
//            }
//        });

		/* 4：申明一个队列Queue */
		// declaring a queue for this channel. If queue does not exist,it will be created on the server.
		channel.queueDeclare(queueName, false, false, false, null);
	}

	/**
	 * 关闭channel和connection。并非必须，因为隐含是自动调用的。
	 * 
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public void close() throws IOException, TimeoutException {
		this.channel.close();
		this.connection.close();
	}
}