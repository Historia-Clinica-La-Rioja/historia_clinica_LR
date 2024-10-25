package commercialmedication.cache.configuration;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Getter
@Component
public class CommercialMedicationSoapRestTemplate extends RestTemplate {

	@Value("${commercial-medication.api.url}")
	private String baseUrl;

	public HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.TEXT_XML_VALUE);
		headers.set("SOAPAction", "#POST");
		return headers;
	}

}
