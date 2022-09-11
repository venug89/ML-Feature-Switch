package com.ml.featureswitch.flow;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ml.featureswitch.pojo.FeatureAccessRequest;
import com.ml.featureswitch.pojo.FeatureAccessResponse;
import com.ml.featureswitch.util.InvalidRequestException;
import com.ml.featureswitch.util.InvalidResponseException;
import com.ml.featureswitch.util.ValidatorUtil;

public class FeatureAccessUnitFlow {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	private static Logger logger = LogManager.getLogger(FeatureAccessUnitFlow.class);
	
	public FeatureAccessRequest generateRequest(Exchange ex) throws InvalidRequestException{
		
		FeatureAccessRequest req = new FeatureAccessRequest();
		
		String query = ex.getIn().getHeader(Exchange.HTTP_QUERY, String.class);
		
		if(query != null){
			
			Map<String, String> queryMap = new LinkedHashMap<String, String>();
		    String[] queryData = query.split("&");
		    for (String qd : queryData) {
		        int i = qd.indexOf("=");
		        try {
		        	queryMap.put(URLDecoder.decode(qd.substring(0, i), "UTF-8"), URLDecoder.decode(qd.substring(i + 1), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new InvalidRequestException();
				}
		    }

		    if(queryMap.containsKey("email") && queryMap.containsKey("featureName") ) {
			    req.setEmail(queryMap.get("email"));
			    req.setFeatureName(queryMap.get("featureName"));
		    } else {
		    	
		    	throw new InvalidRequestException();
		    }
		    
		} else {
			
			throw new InvalidRequestException();
		}
		
		return req;
		
	}
	
	
	public void validateRequest(FeatureAccessRequest request) throws InvalidRequestException{
		
		String featureName = request.getFeatureName();
		String email = request.getEmail();
		
		if(ValidatorUtil.isNullOrEmpty(featureName)){
			logger.info("Feature Name cannot be empty");
			throw new InvalidRequestException();
		}
		
		if(ValidatorUtil.isNullOrEmpty(email)){
			logger.info("Email cannot be empty");
			throw new InvalidRequestException();
		} else {
			if (!ValidatorUtil.isEmail(email)) {
				logger.info("Invalid Email");
			throw new InvalidRequestException();	
				
			}
		}
		
		logger.info("Request Object for Feature Access -- Email : "+request.getEmail()+ ", Feature Name : "+request.getFeatureName());
		
	}
	
	public FeatureAccessResponse generateResponse(FeatureAccessRequest req,Exchange ex ) throws InvalidResponseException{
		
		FeatureAccessResponse response = new FeatureAccessResponse();
		
		String q = "select isEnable from featureswitch inner join user on user.id = featureswitch.userId inner join feature on feature.id = featureswitch.featureId where email=? and featureName=?";
				
		boolean isEnable;
		
		try {
			
			isEnable = jdbcTemplate.queryForObject(q, new Object[]{req.getEmail(),req.getFeatureName()}, boolean.class);
		
			response.setCanAccess(isEnable);
		}
    	catch(Exception e) {
			
			throw new InvalidResponseException(e.getMessage());
		}
		
		return response;
	} 
	
	public FeatureAccessResponse generateErrorResponse(Exception e){
		
		FeatureAccessResponse res = new FeatureAccessResponse();
		
		if( e instanceof InvalidRequestException ) {
			logger.info("Invalid Request");
			res.setCanAccess(false);
		}
		else if( e instanceof InvalidResponseException ) {
			logger.info("Invalid Response");
			logger.info("Actual Error : "+e.getMessage());
			res.setCanAccess(false);
		}	
		
		return res;
	}

}
