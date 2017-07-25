package com.javapai.practice.springdata;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;

@Controller
public class TestSpringDataMongodb {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void searchTest() {
		Ncallrecords users = new Ncallrecords();
		users.setUserId("8");
		
		mongoTemplate.getCollection("ncallrecords");

		List<Ncallrecords> list = mongoTemplate.find(new Query(Criteria.where("userId").is(users.getUserId())), Ncallrecords.class);
		System.out.println("edit user mongodb:" + list);
		// System.out.println(">>>"+messageResult.getData().toString());
	}

}
