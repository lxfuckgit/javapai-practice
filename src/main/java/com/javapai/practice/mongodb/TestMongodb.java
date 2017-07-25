package com.javapai.practice.mongodb;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * MySQL:database>table>>>>>>>row>>>>>>>column
 * Mongo:database>collections>documnent>fields
 * 
 * @author liuxiang
 *
 */
public class TestMongodb {
	/*Mongodb2X*/
//	public static String server="192.168.0.165";
//	public static int port = 27017;
	
	/*Mongodb3X*/
	public static String server = "192.168.1.195";
	public static int port = 37037;
	
	public static String database = "mobp2p";
	public static String collection = "ncallrecords";

	public static void main(String[] args) throws UnknownHostException {
		/**
		 * 测试结果：<br>
		 * 用3.x驱动连接mongo2.x，当出现value=undefined的key时，是可以正常读取的(undefine类型)。
		 * 用3.x驱动连接mongo3.x，当出现value=undefined的key时，是可以正常读取的(undefine类型)。
		 * <br><br>
		 * 用2.x驱动连接mongo2.x，当出现value=undefined的key时，可以正常读取，但undefined对应的key被忽略查询返回。
		 * 用2.x驱动连接mongo3.x，当出现value=undefined的key时，可以正常读取，但undefined对应的key被忽略查询返回。
		 */
//		TestMongodb.testMongoJavaDriver24(server, port, database);
//		TestMongodb.testMongoJavaDriver32(server, port, database);
		
		TestMongodb.testSpringDataMongodb(database);
	}

	private static MongoClient getConnection(String ip, int port) throws UnknownHostException {
		// TODO Auto-generated method stub
		//method1:
		MongoClient mongoClient = new MongoClient(ip, port);

		// method2:或者像这样连接到一个副本集，需要提供一个列表
		// MongoClient mongoClient = new MongoClient(Arrays.asList(new
		// com.mongodb.ServerAddress("localhost", 27017),
		// new com.mongodb.ServerAddress("localhost", 27018)));

		// method3:或者使用连接字符串
//		MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017,localhost:27018");
//		MongoClient mongoClient = new MongoClient(connectionString);
		return mongoClient;
	}

	/**
	 * 测试mongo-java-driver驱动(java-driver=3.x)
	 * 
	 * @param databaseName
	 * @return
	 * @throws UnknownHostException
	 */
//	public static void testMongoJavaDriver32(String server, int port, String databaseName) throws UnknownHostException {
//		MongoClient mongoClient = getConnection(server, port);
//		// 获取到数据库对象mydb，如果不存在则自动创建
//		com.mongodb.client.MongoDatabase database = mongoClient.getDatabase(databaseName);
//
//		// 获取一个集合
//		com.mongodb.client.MongoCollection<org.bson.Document> collections = database.getCollection(collection);
//		System.out.println("此集合现有文档数量：" + collections.count());
//
//		// 显示一个集合中的文档
//		com.mongodb.client.FindIterable<org.bson.Document> list = collections.find();
//		for (org.bson.Document document : list) {
//			if("f6af3ec7f1ac4a9d9c6f072b78c7357d".equals(document.getString("_id"))) {
//				System.out.println("result=" + document.toString());
//			}
//		}
//
//		// 向集合中插入一个文档
//		org.bson.Document doc = new org.bson.Document();
//		doc.append("user_id", "222333");// key->value
//		collections.insertOne(doc);
//		System.out.println(">>>正在插入数据:user_id->222333");
//
//		// 向集合中删除一个文档
//		collections.deleteOne(doc);
//		System.out.println(">>>正在删除数据:user_id->222333");
//	}

	/**
	 * 测试mongo-java-driver驱动(java-driver=2.x)
	 * 
	 * @param server
	 * @param port
	 * @param databaseName
	 * @throws UnknownHostException
	 */
//	public static void testMongoJavaDriver24(String server, int port, String databaseName) throws UnknownHostException {
//		MongoClient mongoClient = getConnection(server, port);
//
//		com.mongodb.DB database = mongoClient.getDB(databaseName);
//		System.out.println(">>>>>驱动版本信息!");
//		com.mongodb.DBCollection coll = database.getCollection(collection);
//		System.out.println(">>>>>" + mongoClient.getVersion());
//		System.out.println(">>>>>" + mongoClient.getMajorVersion());
//		System.out.println(">>>>>" + mongoClient.getMinorVersion());
//
//		com.mongodb.DBObject object = coll.findOne();
//	    System.out.println("固定查询："+object.toString());
//	}
	
	public static void testSpringDataMongodb(String databaseName) throws UnknownHostException {
		MongoClient mongoClient = getConnection(server, port);
		/**
		<mongo:mongo host="localhost" port="27017"/>
		  
		  <bean id="mongoTemplate" class="org.springframework.data.mongodb.core.MongoTemplate">
		    <constructor-arg ref="mongo"/>
		    <constructor-arg name="databaseName" value="${databaseName}"/>
		  </bean>
		*/
		MongoOperations template = new MongoTemplate(new SimpleMongoDbFactory(mongoClient, databaseName));
		com.mongodb.DBObject object = template.getCollection(collection).findOne();
		System.out.println("固定查询："+object.toString());
	}

}
