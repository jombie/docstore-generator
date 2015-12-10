package com.m2f.docstore.couchbase;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.fluttercode.datafactory.impl.DataFactory;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.document.JsonArrayDocument;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import com.couchbase.client.java.query.Index;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.query.dsl.Sort;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;
import com.google.gson.Gson;
import com.m2f.docstore.model.BusinessRegistration;
import com.m2f.docstore.model.BusinessRegistration.BusinessDetails;
import com.m2f.docstore.model.BusinessRegistration.SearchTags;

public class CouchbaseTest {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder().queryEnabled(true).build();
		Cluster cluster = CouchbaseCluster.create(env);
		String bucketName = "couchbase_bucket";//"memcache_bucket";
		Bucket bucket = cluster.openBucket(bucketName, "couchbase", 20, TimeUnit.SECONDS);
		//listBuckets(cluster);
		//delete(bucketName, bucket, 500000, 900000);
		//insert(bucket, 1000, 0, 320000);
		insertBlocking(bucket, 1, 100000);
		//index(bucketName, bucket);
		//createSecondaryIndex(bucketName, bucket, "pincode");
		//query(bucketName, bucket);
		//queryAsync(bucketName, bucket);
		//queryByView(bucketName, bucket);
		
//		int totalIds = 10;
//		String[] ids = new String[totalIds];
//		for(int i = 1; i <= totalIds; i++){
//			ids[i - 1] = "business_" + i;
//		}
//		get(bucketName, bucket, ids);
	}

	private static void listBuckets(Cluster cluster) {
		ClusterManager clusterManager = cluster.clusterManager("admin", "couchbase");
		List<BucketSettings> buckets = clusterManager.getBuckets();
		System.out.println("Buckets are : ");
		for (BucketSettings bucketSettings : buckets) {
			System.out.println(bucketSettings.name());
		}
	}

	private static JsonArrayDocument insertA(Bucket bucket) {
		DataFactory df = DataFactory.create(19823512);
		Gson gson = new Gson();
		JsonArray businesses = JsonObject.ja();
		for(int i = 0; i < 1353; i++){
            SearchTags searchTags = new SearchTags(df.getRandomText(30), df.getRandomWord(), df.getRandomWord());
            BusinessDetails businessDetail = new BusinessDetails(df.getRandomAlphaNumericCaps(10), df.getRandomAlphaNumericCaps(10),df.getRandomAlphaNumericCaps(10),df.getAddress());
            BusinessRegistration b = 
            		new BusinessRegistration(df.getName(), df.getFirstName().toLowerCase(),
            				df.getEmailAddress(), df.getRandomChars(8), df.getMobileNumber(),
            				df.getMobileNumber(), df.getRealNumberUpTo(100), df.getRealNumberUpTo(100), 
            				df.getAddress(), df.getPincode(), df.getRandomText(50), df.getBusinessType(), 
            				df.getRandomChars(5), df.getRandomWord(), df.getBooleanValue(), df.getBooleanValue(), 
            				df.getRandomWord(), df.getBooleanValue(), searchTags, businessDetail);
            String businessDoc = gson.toJson(b);
            JsonObject jsonObj = JsonObject.fromJson(businessDoc);
            businesses.add(jsonObj);
		}
		JsonArrayDocument doc = JsonArrayDocument.create("businesses", businesses);
		JsonArrayDocument response = bucket.upsert(doc);
		System.out.println("Inserted document size " + response.content().toString().getBytes().length / 1024);
		return response;
	}

	private static void insert(final Bucket bucket, int flushInterval, int startId, int endId) {
		DataFactory df = DataFactory.create(19823512);
		Gson gson = new Gson();
		List<JsonDocument> documents = new ArrayList<JsonDocument>();
		for(int i = startId; i <= endId; i++){
            SearchTags searchTags = new SearchTags(df.getRandomText(30), df.getRandomWord(), df.getRandomWord());
            BusinessDetails businessDetail = new BusinessDetails(df.getRandomAlphaNumericCaps(10), df.getRandomAlphaNumericCaps(10),df.getRandomAlphaNumericCaps(10),df.getAddress());
            BusinessRegistration b = 
            		new BusinessRegistration(df.getName(), df.getFirstName().toLowerCase(),
            				df.getEmailAddress(), df.getRandomChars(8), df.getMobileNumber(),
            				df.getMobileNumber(), df.getRealNumberUpTo(100), df.getRealNumberUpTo(100), 
            				df.getAddress(), df.getPincode(), df.getRandomText(50), df.getBusinessType(), 
            				df.getRandomChars(5), df.getRandomWord(), df.getBooleanValue(), df.getBooleanValue(), 
            				df.getRandomWord(), df.getBooleanValue(), searchTags, businessDetail);
            String businessDoc = gson.toJson(b);
            JsonObject jsonObj = JsonObject.fromJson(businessDoc);
    		JsonDocument doc = JsonDocument.create("business_" + i, jsonObj);
    		documents.add(doc);
            if(i % flushInterval == 0) {
            	List<JsonDocument> documentsC = new ArrayList<JsonDocument>(documents);
            	Observable
                .from(documentsC)
                .flatMap(new Func1<JsonDocument, Observable<JsonDocument>>() {
                    @Override
                    public Observable<JsonDocument> call(final JsonDocument docToInsert) {
                        return bucket.async().insert(docToInsert);
                    }
                }).subscribe(new Action1<JsonDocument>() {
					@Override
					public void call(JsonDocument t) {
					}
				}, new Action1<Throwable>() {
					@Override
					public void call(Throwable t) {
						System.err.println("Error inserting " + t.getMessage());
					}
				});
            	documents.clear();
            	System.out.println("Inserted " + (i - startId + 1) + " documents");
            }
		}
	}

	private static void insertBlocking(final Bucket bucket, int startId, int endId) {
		DataFactory df = DataFactory.create(19823512);
		Gson gson = new Gson();
		for(int i = startId; i <= endId; i++){
            SearchTags searchTags = new SearchTags(df.getRandomText(30), df.getRandomWord(), df.getRandomWord());
            BusinessDetails businessDetail = new BusinessDetails(df.getRandomAlphaNumericCaps(10), df.getRandomAlphaNumericCaps(10),df.getRandomAlphaNumericCaps(10),df.getAddress());
            BusinessRegistration b = 
            		new BusinessRegistration(df.getName(), df.getFirstName().toLowerCase(),
            				df.getEmailAddress(), df.getRandomChars(8), df.getMobileNumber(),
            				df.getMobileNumber(), df.getRealNumberUpTo(100), df.getRealNumberUpTo(100), 
            				df.getAddress(), df.getPincode(), df.getRandomText(50), df.getBusinessType(), 
            				df.getRandomChars(5), df.getRandomWord(), df.getBooleanValue(), df.getBooleanValue(), 
            				df.getRandomWord(), df.getBooleanValue(), searchTags, businessDetail);
            String businessDoc = gson.toJson(b);
            JsonObject jsonObj = JsonObject.fromJson(businessDoc);
    		JsonDocument doc = JsonDocument.create("business_" + i, jsonObj);
    		bucket.insert(doc, 60000, TimeUnit.SECONDS);
    		if(i % 1000 == 0) System.out.println("Inserted upto " + i  + " documents"); 
		}
	}

	private static void index(String bucketName, Bucket bucket) {
		System.out.println("Creating primary index: ");
		Statement indexQuery = Index.createPrimaryIndex().on(bucketName);
		N1qlQueryResult indexResult = bucket.query(N1qlQuery.simple(indexQuery));
        if (indexResult.finalSuccess()) {
            System.out.println("Successfully created primary index.");
        } else {
        	System.err.println("Could not create primary index: " + indexResult.errors());
        }
		String buildIndex = "BUILD INDEX ON `couchbase_bucket`(`#primary`)  USING GSI";
		bucket.query(N1qlQuery.simple(buildIndex));
	}

	private static void query(String bucketName, Bucket bucket) throws IOException {
		long start = System.currentTimeMillis();
		//N1qlQueryResult result = bucket.query(select("name, userName, pincode, businessDetails.panNumber").from(i(bucketName)).limit(3));
		N1qlQueryResult result = 
				bucket.query(select("name, userName, pincode, businessDetails.panNumber")
						.from(i(bucketName))
						.where(x("pincode").eq(560068).and(x("userName").like(s("p%"))))
						.orderBy(Sort.asc("userName"))
						.limit(100).offset(0));
		if (!result.finalSuccess()) {
            System.err.println("Query returned with errors: " + result.errors());
            throw new IOException("Query error: " + result.errors());
        }
		long end = System.currentTimeMillis();
        for (N1qlQueryRow row : result) {
        	System.out.println(row);
        }
        System.out.println("Time taken " + (end - start) + " ms");
	}
	
	private static void queryAsync(String bucketName, Bucket bucket) throws InterruptedException{
		final CountDownLatch latch = new CountDownLatch(1);
		final long start = System.currentTimeMillis();
		N1qlParams params = N1qlParams.build().adhoc(false);
		Statement stmt = select("pincode").from(i(bucketName)).where(x("pincode").between(560001).and(560068)).limit(100).offset(0);
		bucket.async()
		.query(N1qlQuery.simple(stmt, params))
				.subscribe(new Action1<AsyncN1qlQueryResult>() {
					@Override
					public void call(AsyncN1qlQueryResult result) {
						result.rows().map(new Func1<AsyncN1qlQueryRow, JsonObject>() {
							@Override
							public JsonObject call(AsyncN1qlQueryRow row) {
								return row.value();
							}
						})
						.subscribe(new Action1<JsonObject>() {
							@Override
							public void call(JsonObject rowContent) {
								//System.out.println(rowContent);
								latch.countDown();
							}
						});
					}
				});
		latch.await();
		long end = System.currentTimeMillis();
		System.out.println("Time taken " + (end - start) + " ms");
	}
	
	private static void delete(String bucketName, Bucket bucket, int startId, int endId) throws IOException {
//		String deleteQuery = "DELETE FROM " + bucketName + " b";
//		N1qlQueryResult result = bucket.query(N1qlQuery.simple(deleteQuery));
//		if (!result.finalSuccess()) {
//            System.err.println("Delete Query returned with errors: " + result.errors());
//            throw new IOException("Query error: " + result.errors());
//        }
		for(int i = startId; i < endId; i++) {
			bucket.remove("business_" + i, 10, TimeUnit.SECONDS);
			if(i % 1000 == 0) System.out.println("Removed " + i + " items" );
		}
		
	}

	private static void createSecondaryIndex(String bucketName, Bucket bucket, String... fieldNames) throws InterruptedException {
		for (String fieldName : fieldNames) {
			Statement query = Index.createIndex(fieldName + "_idx").on(bucket.name(), x(fieldName)).withDefer();
            N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
            if (result.finalSuccess()) {
                System.out.println("Successfully created index with name " + fieldName + "_idx");
            } else {
                System.err.println("Could not create index for " + fieldName + " " + result.errors());
            }
		}
		System.out.println("Waiting 5 seconds before building the indexes.");
        Thread.sleep(5000);
      //trigger the build
        StringBuilder indexes = new StringBuilder();
        boolean first = true;
        for (String name : fieldNames) {
            if (first) {
                first = false;
            } else {
                indexes.append(",");
            }
            indexes.append(name + "_idx");
        }

        String query = "BUILD INDEX ON `" + bucket.name() + "` (" + indexes.toString() + ")";
        N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));
        if (result.finalSuccess()) {
            System.out.println("Successfully executed build index query.");
        } else {
            System.err.println("Could not execute build index query " + result.errors());
        }
	}
	
	private static void get(String bucketName, Bucket bucket, String... ids) throws InterruptedException{
		final CountDownLatch latch = new CountDownLatch(1);
		final long start = System.currentTimeMillis();
		JsonDocument doc = null;
		for (String id : ids) {
			doc = bucket.get(id);
		}
		final long end = System.currentTimeMillis();
		System.out.println(doc.content().toString());
		System.out.println("Time taken " + (end - start));
	}
	
	private static void queryByView(String bucketName, Bucket bucket) {
		long start = System.currentTimeMillis();
		ViewResult result = bucket.query(ViewQuery.from("business", "pincode_business")
				.startKey(560001).endKey(560068).limit(10));
		for (ViewRow row : result) {
		    JsonDocument response = bucket.get(row.id());
		    //System.out.println(row.id() + ", " + row.key() + ", " + response.content().getString("userName"));
		}
		long end = System.currentTimeMillis();
		System.out.println("Time taken " + (end - start));
	}
}
