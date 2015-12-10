package com.m2f.docstore.elasticsearch;

import java.net.InetSocketAddress;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.*;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.node.Node;

import static org.elasticsearch.node.NodeBuilder.*;

public class ElasticsearchTest {
	
	public static void main(String[] args) {
		//nodeClient();
		//transportClient();
		test();
	}
	
	public static void transportClient(){
		Client client = null;
		try {
			Settings settings = ImmutableSettings.settingsBuilder()
			        .put("cluster.name", "elastic").build();
			
			client = new TransportClient(settings)
			.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
			MatchQueryBuilder query = matchQuery("category", "North");
			SearchResponse response = search(client, query); 
			System.out.println(response.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != client) client.close();
		}
	}
	
	public static void nodeClient(){
		Node node = null;
		try {
			String clusterName = "elastic";
			Settings settings = ImmutableSettings.settingsBuilder()
			        .put("http.enabled", false)
			        .put("transport.tcp.port", "9300-9400")
			        .put("discovery.zen.ping.multicast.enabled", "false")
			        .put("discovery.zen.ping.unicast.hosts", "localhost").build();

			node = nodeBuilder()
					.clusterName(clusterName)
					.settings(settings)
					.client(true)
					.node();
			Client client = node.client();
			MatchQueryBuilder query = matchQuery("category", "North");
			SearchResponse response = search(client, query); 
			System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != node) node.close();
		} 
	}
	
	public static SearchResponse search(Client client, QueryBuilder query){
		SearchRequestBuilder requestBuilder = 
				client.prepareSearch("beer-sample")
				.setTypes("couchbaseDocument")
				.setSearchType(SearchType.DFS_QUERY_AND_FETCH)
				.setQuery(query)
				.setFrom(0)
				.setSize(50)
				.setExplain(true);
		System.out.println("Request " + requestBuilder.toString());
		SearchResponse response = requestBuilder.execute().actionGet();
		return response;
	}
	
	public static void test(){
		TermQueryBuilder tqb = termQuery("category", "North American Ale");
		System.out.println(tqb.toString() + "\n==========================================");
		MatchQueryBuilder mqb = matchQuery("category", "North");
		System.out.println(mqb.toString() + "\n==========================================");
		QueryBuilder qs = queryStringQuery("elasticsearch");
		System.out.println(qs.toString() + "\n==========================================");
		QueryBuilder qmm = multiMatchQuery("kimchy elasticsearch","user", "message");
		System.out.println(qmm.toString() + "\n==========================================");
		QueryBuilder qb = boolQuery()
			    .must(termQuery("content", "test1"))    
			    .must(termQuery("content", "test4"))    
			    .mustNot(termQuery("content", "test2")) 
			    .should(termQuery("content", "test3"));
		System.out.println(qb.toString());
		QueryBuilder qb1 = boostingQuery()
			    .positive(termQuery("name","kimchy"))   
			    .negative(termQuery("name","dadoonet")) 
			    .negativeBoost(0.2f);
		System.out.println(qb1.toString());
		QueryBuilder qb2 = idsQuery().ids("1", "2");
		System.out.println(qb2.toString());
		QueryBuilder qb3 = fuzzyQuery(
			    "name",     
			    "kimzhy"    
			);
		System.out.println(qb3.toString());
		QueryBuilder qb4 = queryStringQuery("+kimchy -elasticsearch");
		System.out.println(qb4.toString());
	}
}
