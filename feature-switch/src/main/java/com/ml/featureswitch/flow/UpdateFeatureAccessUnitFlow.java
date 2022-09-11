package com.ml.featureswitch.flow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ml.featureswitch.pojo.UpdateFeatureAccessRequest;
import com.ml.featureswitch.util.InvalidRequestException;
import com.ml.featureswitch.util.InvalidResponseException;
import com.ml.featureswitch.util.ValidatorUtil;

public class UpdateFeatureAccessUnitFlow {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static Logger logger = LogManager.getLogger(UpdateFeatureAccessUnitFlow.class);
	
	public void validateRequest(UpdateFeatureAccessRequest request) throws InvalidRequestException{
		
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
		
		logger.info("Request Object for Update Feature -- Email : "+request.getEmail()+ ", Feature Name : "+request.getFeatureName()+ ", Enable : "+request.isEnable());
	}
	
	public void updateFeature(UpdateFeatureAccessRequest request) throws InvalidResponseException{
		
		String q = "update featureswitch inner join user on user.id = featureswitch.userId inner join feature on feature.id = featureswitch.featureId set isEnable=? where email=? and featureName=?";
		
		try {
			
			int	s = jdbcTemplate.update(q, new Object[]{request.isEnable(),request.getEmail(),request.getFeatureName()});
			
			if(s == 0){
				logger.info("Database is not updated");
				throw new InvalidResponseException();
			}
		
		}
    	catch(Exception e) {
    		throw new InvalidResponseException(e.getMessage());
		} 
	}
	
}
