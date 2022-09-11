package com.ml.featureswitch;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.ml.featureswitch.flow.FeatureAccessUnitFlow;
import com.ml.featureswitch.flow.UpdateFeatureAccessUnitFlow;
import com.ml.featureswitch.pojo.FeatureAccessResponse;
import com.ml.featureswitch.pojo.UpdateFeatureAccessRequest;
import com.ml.featureswitch.util.InvalidRequestException;
import com.ml.featureswitch.util.InvalidResponseException;

@Component
public class FeatureSwitchRouteBuilder {
	
	@Bean
	RouteBuilder getFeatureAccess() {
		return new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				
				onException(InvalidRequestException.class)
				.handled(true)				
				.bean(FeatureAccessUnitFlow.class, "generateErrorResponse");
				
				onException(InvalidResponseException.class)
				.handled(true)				
				.bean(FeatureAccessUnitFlow.class, "generateErrorResponse");
				
				restConfiguration()
				.component("servlet")
				.port(8080)
				.bindingMode(RestBindingMode.auto);

				rest("/feature/").id("get-feature-access-route")		
				.get()
				.outType(FeatureAccessResponse.class)
                .produces("application/json")
                .route()
                .bean(FeatureAccessUnitFlow.class, "generateRequest")
				.bean(FeatureAccessUnitFlow.class, "validateRequest")
				.bean(FeatureAccessUnitFlow.class, "generateResponse");					
			}
			};
		}
	
	
	@Bean
	RouteBuilder updateFeatureAccess() {
		return new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				
				onException(InvalidRequestException.class)
				.handled(true)				
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(304))
				.setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
				.setBody(constant("Not Modified"));
				
				onException(InvalidResponseException.class)
				.handled(true)				
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(304))
				.setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
				.setBody(constant("Not Modified"));
				
				onException(InvalidFormatException.class)
				.handled(true)				
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(304))
				.setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
				.setBody(constant("Not Modified"));
	
				restConfiguration()
				.component("servlet")
				.port(8080)
				.bindingMode(RestBindingMode.auto);

				rest("/feature/").id("update-feature-access-route")		
				.post().type(UpdateFeatureAccessRequest.class)
                .route()
				.bean(UpdateFeatureAccessUnitFlow.class, "validateRequest")
				.bean(UpdateFeatureAccessUnitFlow.class, "updateFeature")
				.setBody().simple("${null}");
			}
			};
		}

}
