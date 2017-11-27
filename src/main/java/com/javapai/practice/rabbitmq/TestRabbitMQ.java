package com.javapai.practice.rabbitmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.rabbitmq.client.AMQP.Queue.BindOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @author liuxiang
 *
 */
public class TestRabbitMQ {
	/**/
	static String host="192.168.33.132";//"192.168.33.132"
	static int port=5672;//5672
	static String user="admin";//admin
	static String pwd="admin";//admin
	
	/* 默认队列名称 */
	static String DEFAULT_QUEUE1_NAME="com.sjd.test-queue1";
	static String DEFAULT_QUEUE2_NAME="com.sjd.test-queue2";
	
	static String DEFAULT_EXCHANGE_NAME = "com.sjd.test-exchange";
	
	static String DEFAULT_MESSAGE="hello,rabbitMQ!";
	
	/**
	 * RabbitMQ应用AMQP开放端口为5672,后台adminUI开放端口为15672。
	 * @param args
	 */
	public static void main(String[] args) {
		TestRabbitMQ test = new TestRabbitMQ();
		System.out.println("----------------------------RabbitMQ消息通信过程----------------------------------------");
		System.out.println("绑定决定了消息是如何从路由器路由到特定的队列。");
//		http://www.cnblogs.com/luxiaoxun/p/3918054.html
//		http://blog.csdn.net/lmj623565791/article/details/37620057
//		http://www.cnblogs.com/me-sa/archive/2012/10/20/RabbitMQ_VHost_Exchanges_queues_bindings_and_Channels.html
		
		try {
			/* 场景1：单发送单接收 */
//			test.testProducer1();
//			test.testConsumer1();
//			
			/* 场景2：单发送多接收(分布式的任务派发) */
//			test.testProducer2();
//			test.testConsumer2();
			
			/* 场景2：Publish/Subscribe(发送端发送广播消息，多个接收端接收) */
			test.testProducer3();
			test.testConsumer3();//为什么这样收不到(后来发现是要在消费者准备接收的情况下，生产者的消息才能被接收到，什么机制这是？要是消费者死了，那生产者不搞死MQ-Server)
			
			/* 场景4：Routing (按路线发送接收) */
//			test.testProducer4();
//			test.testConsumer4();
			
			/*场景5：Topics (按topic发送接收)*/
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		
//		能够管理exchange(包括创建，修改，删除)。
//		2、创建后的exchange需要审核通过后才能使用。
//		3、能够根据项目查询其下的exchange、queue和channel。
//		4、能够断开某个消费者和rabbitMQ的消费关系。
	}
	
	@Test
	public void testProducer1() throws IOException, TimeoutException {
		System.out.println("=====================MQ生产者1正在启动=====================");
		Channel channel = createChannel();
		channel.queueDeclare(DEFAULT_QUEUE1_NAME, false, false, false, null);
		// 默认Exchange名称为""空字符串
		channel.basicPublish("", DEFAULT_QUEUE1_NAME, null, DEFAULT_MESSAGE.getBytes());
		System.out.println(">>>>>>>>>>>生产者已投递消息[" + DEFAULT_MESSAGE + "]到exchange.");
		channel.close();
	}
	
	@Test
	public void testConsumer1() throws IOException, TimeoutException {
		System.out.println("=====================MQ队列消费者1正在启动=====================");
		Channel channel = createChannel();
		channel.queueDeclare(DEFAULT_QUEUE1_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	    
	    QueueingConsumer consumer = new QueueingConsumer(channel);
	    channel.basicConsume(DEFAULT_QUEUE1_NAME, true, consumer);

		while (true) {
			try {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody(),"UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			} catch (ShutdownSignalException | ConsumerCancelledException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testProducer2() throws IOException, TimeoutException {
		System.out.println("=====================MQ生产者2正在启动=====================");
		Channel channel = createChannel();
		
		//1:声明了一个新Queue，因为RabbitMQ不容许声明2个相同名称、配置不同的Queue(如果你声明一个已存在的queue名且配置不一样，就会报错)。
		//2:为保证消发送的可靠性，不丢失消息，设置Queue的durable的属性为true；使消息队列具有durable持久化特性。
		channel.queueDeclare(DEFAULT_QUEUE2_NAME, true, false, false, null);
		//这种情况下没有exchange吗，有默认的exchange？名叫什么？
		
		/* 多发几条试试ACK */
		for (int i = 0; i < 10; i++) {
			HashMap<String, String> message = new HashMap<String, String>();
			message.put("mq-body", "我是(" + i + ")号消息报文体.");
			
			//3:使用MessageProperties.PERSISTENT_TEXT_PLAIN使消息durable。
			channel.basicPublish("",DEFAULT_QUEUE2_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, SerializationUtils.serialize(message));
			System.out.println(">>>>>>>>>>>生产者已投递消息[" + message.get("mq-body") + "]到exchange.");
		}
	}
	
	@Test
	public void testConsumer2() throws IOException, TimeoutException {
		System.out.println("=====================MQ队列消费者2正在启动=====================");
		Channel channel = createChannel();

		//声明队列具有durable特性(不声明的情况下，消费DEFAULT_QUEUE2_NAME会有什么情况?)
		channel.queueDeclare(DEFAULT_QUEUE2_NAME, true, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		// 保证公平分发
		channel.basicQos(1);//保证在接收端一个消息没有处理完时不会接收另一个消息，即接收端发送了ack后才会接收下一个消息。在这种情况下发送端会尝试把消息发送给下一个not busy的接收端。

		QueueingConsumer consumer = new QueueingConsumer(channel);
		//在使用channel.basicConsume接收消息时使autoAck为false，即不自动会发ack，由channel.basicAck()在消息处理完成后发送消息。
		channel.basicConsume(DEFAULT_QUEUE2_NAME, false, consumer);

		while (true) {
			try {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println(" [x] Received '" + message + "'");
				
				//为体现公平分发特性，故意sleep.
				Thread.sleep(1000);
				System.out.println(" [x] Done");

				//如果不ack，好像会有内存占用问题.
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			} catch (ShutdownSignalException | ConsumerCancelledException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void testProducer3() throws IOException, TimeoutException {
		System.out.println("=====================MQ生产者3正在启动=====================");
		Channel channel = createChannel();
		//声明exchange时也一样，如果已存在且配置不一样也会报错.(为什么会报错?)
		channel.exchangeDeclare("com.sjd.test-exchange-fanout", "fanout");
		
		//发送消息到一个exchange上，使用“fanout”方式发送，即广播消息，不需要使用queue，发送端不需要关心谁接收。(为什么不用关心？)
		channel.basicPublish("com.sjd.test-exchange-fanout", "", null, DEFAULT_MESSAGE.getBytes());
	    System.out.println(" [x] Sent '" + DEFAULT_MESSAGE + "'");

	    channel.close();
	}
	 
	@Test
	public void testConsumer3() throws IOException, TimeoutException {
		System.out.println("=====================MQ队列消费者3正在启动=====================");
		Channel channel = createChannel();
		//1、声明的exchange及方式和发送端必需一样。
		channel.exchangeDeclare("com.sjd.test-exchange-fanout", "fanout");
		
		//2、得到一个随机名称的Queue，该queue的类型为non-durable、exclusive、auto-delete
		String queueName = channel.queueDeclare().getQueue();
		System.out.println("-----------------随机生成queue:" + queueName);
		
		//3、channel.queueBind()的第三个参数Routing key为空，即所有的消息都接收。如果这个值不为空，在exchange type为“fanout”方式下该值被忽略！
		BindOk ok = channel.queueBind(queueName, "com.sjd.test-exchange-fanout", "");//经测试，绑错了exchange名就收不到信息.
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		String consumerTag = channel.basicConsume(queueName, true, consumer);
		System.out.println(">>>>>MQ队列消费者运行标识：" + consumerTag);

		while (true) {
			try {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println(" [x] Received '" + message + "'");
			} catch (ShutdownSignalException | ConsumerCancelledException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testProducer4() throws IOException, TimeoutException {
		System.out.println("=====================MQ生产者4正在启动=====================");
		Channel channel = createChannel();
	    channel.exchangeDeclare("com.sjd.test-exchange-direct", "direct");
	    
	    Map<String,String> map = new HashMap<>();
	    map.put("info", "this is info message");
	    map.put("error", "this is error message");
	    
	    System.out.println("-------------正在启用direct路由规则--------------");
	    for (String ROUTING_KEY : map.keySet()) {
//	    	String ROUTING_KEY = "info";
		    channel.basicPublish("com.sjd.test-exchange-direct", ROUTING_KEY, null, map.get(ROUTING_KEY).getBytes());
		    System.out.println(" [x] Sent '" + ROUTING_KEY + "':'" + map.get(ROUTING_KEY) + "'");	
		}

	    channel.close();
	}
	
	@Test
	public void testConsumer4() throws IOException, TimeoutException {
		System.out.println("=====================MQ队列消费者4正在启动=====================");
		Channel channel = createChannel();
		channel.exchangeDeclare("com.sjd.test-exchange-direct", "direct");
	    String queueName = channel.queueDeclare().getQueue();
	    
	    channel.queueBind(queueName, "com.sjd.test-exchange-direct", "info");
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    QueueingConsumer consumer = new QueueingConsumer(channel);
	    channel.basicConsume(queueName, true, consumer);

		while (true) {
			try {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				String routingKey = delivery.getEnvelope().getRoutingKey();

				System.out.println(" [x] Received '" + routingKey + "':'" + message + "'");
			} catch (ShutdownSignalException | ConsumerCancelledException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Channel createChannel() throws IOException, TimeoutException {
		/* 1:Create a connection factory */
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setVirtualHost("/");// 默认就有一个名为"/"
		factory.setPort(port);
		factory.setUsername(user);
		factory.setPassword(pwd);

		/* 2:Getting a connection */
		//RabbitMQ建议客户端线程之间不要共用Channel，至少要保证共用Channel的线程发送消息必须是串行的，但是建议尽量共用Connection。
		Connection connection = factory.newConnection();
		System.out.println(">>>>>>>>>>>>>正在连接消息队列服务器......");

		/* 3：Creating a channel */
		Channel channel = connection.createChannel();
		System.out.println(">>>>>>>>>>>>>正在启动一条消息通道["+channel.toString()+"]...........");
		// int prefetchCount = 1;
		// channel.basicQos(prefetchCount); //保证公平分发
//      channel.confirmSelect(); //Enables publisher acknowledgements on this channel
//      channel.addConfirmListener(new com.rabbitmq.client.ConfirmListener() {
//          @Override
//          public void handleNack(long deliveryTag, boolean multiple) throws IOException {
//              System.out.println("[handleNack] :" + deliveryTag + "," + multiple);
//          }
//
//          @Override
//          public void handleAck(long deliveryTag, boolean multiple) throws IOException {
//              System.out.println("[handleAck] :" + deliveryTag + "," + multiple);
//          }
//      });
		
		/* 4：声明Queue,并设置相关属性。 */
		// declaring a queue for this channel. If queue does not exist,it will be created on the server.
//		channel.queueDeclare(queueName, false, false, false, null);
//		System.out.println(">>>>>>>>>>正在指定一个队列quene..........");
		
		/* 5：声明exchange,并设置相关属性。 */
		// declaring a exchange for this channel. If exchange does not exist,it will be created on the server.
//		channel.exchangeDeclare(exchangeName, "topic");
//		System.out.println(">>>>>>>>>>正在指定一个exchange..........");
//		channel.queueBind(queueName, exchangeName, "routingKey_xx");//使用routing key将queue和exchange绑定
//		System.out.println(">>>>>>>>>>正在绑定queue和exchange........");
		
		/* 6：关闭channel和connection(非必须，因为隐含是自动调用)。 */
//		channel.close();//注意：这里先不关，要返回channel
//		connection.close();
		
		return channel;
	};


}
