package com.ml.featureswitch.util;

import org.apache.commons.validator.routines.EmailValidator;;

public class ValidatorUtil {

	
	public static boolean isNullOrEmpty(String value) {
		
		if (value == null)
			return true;
		
		if (value.trim().length() == 0)
			return true;
		else
			return false;
	}
	
	public static boolean isEmail(String value){
		
		boolean b = EmailValidator.getInstance().isValid(value); 
		
		return b;
	}

}