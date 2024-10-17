package net.pladema.medication.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SoapRestTemplate extends RestTemplate {

	public final static String BASE_URL = "http://abws.alfabeta.net:80/alfabeta-webservice/abWsDescargas";

	public HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.TEXT_XML_VALUE);
		headers.set("SOAPAction", "#POST");
		return headers;
	}

}
