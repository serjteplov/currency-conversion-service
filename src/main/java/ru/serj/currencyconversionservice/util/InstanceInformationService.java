package ru.serj.currencyconversionservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class InstanceInformationService {

	// @Value(${ENVIRONMENT_VARIABLE_NAME:DEFAULT_VALUE})
	@Value("${HOSTNAME:UNKNOWN}")
	private String hostName;

	public String retrieveInstanceInfo() {
		return hostName + " v1 " + hostName.substring(hostName.length()-5);
	}

}