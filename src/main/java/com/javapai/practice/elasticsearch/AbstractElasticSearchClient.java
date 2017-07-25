package com.javapai.practice.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public abstract class AbstractElasticSearchClient {
	/**
	 * 创建client.<br>
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static TransportClient createClient(String clusterName, String ip, int host) throws UnknownHostException {
		if ("".equals(clusterName) || null == clusterName) {
			return null;
		}

		// 设置集群名称
		Settings settings = Settings.builder().put("cluster.name", clusterName).build();
		// 创建client
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), host));

		return client;
	}
	
	/**
	 * 创建client.<br>
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static TransportClient createClient(String clusterName, String ...ips) throws UnknownHostException {
		if ("".equals(clusterName) || null == clusterName) {
			return null;
		}

		// 设置集群名称
		Settings settings = Settings.builder().put("cluster.name", clusterName).build();
		// 创建client
		TransportClient client = new PreBuiltTransportClient(settings);
		for (String ip : ips) {
			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), 9300));
		}

		return client;
	}
	
	/**
	 * 增加结点.
	 * 
	 * @param name
	 */
	public static synchronized void addNode(TransportClient client, String name) {
		try {
			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(name), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	// 删除集群中的某个节点
	public static synchronized void removeNode(TransportClient client, String name) {
		try {
			client.removeTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(name), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**/
	public static void getIndexMapping(TransportClient client,String indexName) {
		IndicesAdminClient indicesClient = client.admin().indices();
		
		IndicesExistsResponse indicesResponse = indicesClient.prepareExists(indexName).get();
		if(indicesResponse.isExists()) {
			System.out.println(">>>>>存在一个twitter索引:" + indicesResponse.isExists());
			GetMappingsResponse r = indicesClient.prepareGetMappings(indexName).get();
			ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mapping = r.getMappings();
			mapping.iterator();
		}
	}
	
	/*创建索引*/
	public static IndexResponse createIndex(TransportClient client,String index,String type,String jsondata) {
		IndexResponse indexResponse = client.prepareIndex(index, type).setSource(jsondata).get();
		System.out.println(">>>>>>" + indexResponse);
		return indexResponse;
	}
	
	/*读取索引*/
	public static GetResponse getIndex(TransportClient client) {
		return client.prepareGet("twitter", "tweet", "1").get();
	}
	
	/* 读取集群信息 */
	public static void getCulsterInfo(TransportClient client) {
		ClusterAdminClient clusterClient = client.admin().cluster();
//		clusterClient.health(request);
		ClusterStatsResponse statusR = clusterClient.prepareClusterStats().get();
		System.out.println(">>>>>节点状态："+statusR.getStatus());
		System.out.println(">>>>>" + statusR.toString());
	}
	
	/**
	 * 删除索引.<br>
	 * 
	 * @param client
	 * @param indexName
	 * @return
	 */
	//curl -X DELETE http://{ES IP address}:9200/{index_name}
	public static boolean deleteIndex(TransportClient client, String indexName) {
		boolean ifexists = existsIndex(client, indexName);
		if (!ifexists) {
			System.out.println(">>>>>>>>>不存此在索引(" + indexName + ")");
		}
		DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName).execute().actionGet();
		return dResponse.isAcknowledged();
	}
	
	/**
	 * 检查索引.<br>
	 * 
	 * @param client
	 * @param indexName
	 * @return
	 */
	public static boolean existsIndex(TransportClient client, String indexName) {
		IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);
		IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
		return inExistsResponse.isExists();
	}
	
	public static void updateIndex(TransportClient client) {
//		client.prepareUpdate("ttl", "doc", "1")
//        .setDoc(jsonBuilder()               
//            .startObject()
//                .field("gender", "male")
//            .endObject())
//        .get();
	}
	
	public static void batchCreateIndex() {
		
	}
}
