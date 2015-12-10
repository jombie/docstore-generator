package com.m2f.docstore.couchbase;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

import com.couchbase.client.java.query.Statement;

public class Test {
	
	public static void main(String[] args) {
		Statement stmt = select("pincode").from(i("couchbase"))
				.where(x("pincode").between(560001).and(560068))
				.limit(100).offset(0);
		System.out.println(stmt.toString());
	}
}
