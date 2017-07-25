package com.javapai.practice.elasticsearch;

import java.io.IOException;
import java.net.UnknownHostException;

import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

public class TestElasticSearch extends AbstractElasticSearchClient {
	
	private static int es_port=9300;
	
	private static String es_server="192.168.1.192";
	private static String cluster_name="my-application";
	
//	private static String es_server="192.168.1.195";
//	private static String cluster_name="elasticsearch";
	
	
	public static void main(String[] args) throws UnknownHostException {
		
		String json ="2017-02-22 11:25:38.888 [main-SendThread(192.168.1.114:2181)] DEBUG org.apache.zookeeper.ClientCnxn - Got ping response for sessionid: 0x35a5ec542d30a76 after 2ms";
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
			json = builder.field("logs", json).endObject().string();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		TransportClient client = createClient(cluster_name,es_server,es_port);
		getCulsterInfo(client);
		
		
//		System.out.println(">>>>>显示所有index");
		
		IndexResponse index = createIndex(client, "twitter1", "tweet",json);
		System.out.println(">>>>>创建索引:" + index.getResult());
		
		
		GetResponse getResponse = getIndex(client);
		System.out.println(">>>>>提取索引:" + getResponse);
//		getResponse.getType()
		
		/* 删除索引 */
//		DeleteResponse d_response = client.prepareDelete("twitter", "tweet", "1").get();
//		System.out.println(">>>>" + d_response.getResult());

	}

	
}
