package com.ml.featureswitch.test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ml.featureswitch.pojo.FeatureAccessResponse;
import com.ml.featureswitch.pojo.UpdateFeatureAccessRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FeatureSwitchTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	 /* This test case will test for "200 - OK, check whether response is null" */ 
	
	@Test
	public void featureAccessTest() {
		
	  ResponseEntity<FeatureAccessResponse> response = restTemplate.getForEntity("/ml/feature?email=abcd@gmail.com&featureName=mainScreen", FeatureAccessResponse.class);
	  
	  Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	  
	  Assertions.assertNotNull(response);
	}
	
	/* This test case will test for "200 - OK" */
	
	@Test
	public void updateFeatureTest1(){
		
		UpdateFeatureAccessRequest requestEntity = new UpdateFeatureAccessRequest();
		
		requestEntity.setEmail("xyz@gmail.com");
		requestEntity.setFeatureName("transactionScreen");
		requestEntity.setEnable(true);
		
		ResponseEntity<String> response = restTemplate.postForEntity("/ml/feature", requestEntity, String.class);
		
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/* This test case will test for "304 - Not Modified" */
	
	@Test
	public void updateFeatureTest2(){
		
		UpdateFeatureAccessRequest requestEntity = new UpdateFeatureAccessRequest();
		
		requestEntity.setEmail("xyz@gmail.com");
		requestEntity.setFeatureName("transactionS");
		requestEntity.setEnable(true);
		
		ResponseEntity<String> response = restTemplate.postForEntity("/ml/feature", requestEntity, String.class);
		
		Assertions.assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
		
	}

}
