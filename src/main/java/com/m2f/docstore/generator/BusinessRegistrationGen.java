package com.m2f.docstore.generator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.fluttercode.datafactory.impl.DataFactory;
import org.postgresql.util.PGobject;

import com.bizosys.oneline.dao.WriteBatch;
import com.bizosys.oneline.service.DBService;
import com.bizosys.oneline.util.Configuration;
import com.bizosys.oneline.util.OnelineServerConfiguration;
import com.google.gson.Gson;
import com.m2f.docstore.model.BusinessRegistration;
import com.m2f.docstore.model.BusinessRegistration.BusinessDetails;
import com.m2f.docstore.model.BusinessRegistration.SearchTags;

public class BusinessRegistrationGen {
	
	public static String INSERT_STMT = "INSERT INTO businesses(name, user_name, email, password, contact_number, "
			+ "alternate_number, location_lat, location_lon, address, pincode, description, category, sub_category, "
			+ "photo_url, type_of_verification, search_tags, business_details) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static void main(String[] args) {
		int len = args.length;
		int totalRecords = len > 0 ? Integer.parseInt(args[0]) : 1000000;
		int flushInterval = 1000;
		DataFactory df = DataFactory.create(19823512);
        Gson gson = new Gson();
        Configuration conf = OnelineServerConfiguration.getInstance().getConfiguration();
        DBService.getInstance().serviceStart(conf);
        WriteBatch writer = new WriteBatch();
        List<Object[]> records = new ArrayList<Object[]>(flushInterval);
        long start = 0;
        long end = 0;
        for(int i = 1; i <= totalRecords; i++) {
            SearchTags searchTags = new SearchTags(df.getRandomText(30), df.getRandomWord(), df.getRandomWord());
            BusinessDetails businessDetail = new BusinessDetails(df.getRandomAlphaNumericCaps(10), df.getRandomAlphaNumericCaps(10),df.getRandomAlphaNumericCaps(10),df.getAddress());
            BusinessRegistration b = 
            		new BusinessRegistration(df.getName(), df.getFirstName().toLowerCase(),
            				df.getEmailAddress(), df.getRandomChars(8), df.getMobileNumber(),
            				df.getMobileNumber(), df.getRealNumberUpTo(100), df.getRealNumberUpTo(100), 
            				df.getAddress(), df.getPincode(), df.getRandomText(50), df.getBusinessType(), 
            				df.getRandomChars(5), df.getRandomWord(), df.getBooleanValue(), df.getBooleanValue(), 
            				df.getRandomWord(), df.getBooleanValue(), searchTags, businessDetail);
        	try {
	            Object[] record = new Object[]
	            		{b.getName(), b.getUserName(), b.getEmail(), b.getPassword(), b.getContactNumber(), 
	            		b.getAlternateNumber(), b.getLocationLat(), b.getLocationLon(), b.getAddress(), b.getPincode(),
	            		b.getDescription(), b.getCategory(), b.getSubCategory(), b.getPhotoUrl(), b.getTypeOfVerification(),
	            		getPGJsonObject(gson.toJson(b.getSearchTags())), getPGJsonObject(gson.toJson(b.getBusinessDetails()))};
	            records.add(record);
	            if(i % flushInterval == 0) {
	            	start = System.currentTimeMillis();
					writer.executeBatch(INSERT_STMT, records);
					end = System.currentTimeMillis();
	            	System.out.println("Total Inserted " + i + " records. CurrentBatch " + records.size() + " time taken " + (end - start) + " ms");
	            	records.clear();
	            }
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(e.getNextException());
			}
        }
	}
	
	private static PGobject getPGJsonObject(String json) throws SQLException{
		PGobject jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(json);
		return jsonObject;
	}
}

