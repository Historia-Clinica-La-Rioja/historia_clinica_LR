package ar.lamansys.sgx.shared.restclient.configuration.resttemplate;

import org.springframework.http.client.ClientHttpRequestInterceptor;

public class RestTemplateAuth<I extends ClientHttpRequestInterceptor> extends RestTemplateSSL {

	public RestTemplateAuth(I authInterceptor) throws Exception {
		super();
		this.getInterceptors().add(authInterceptor);
	}

	public RestTemplateAuth(I authInterceptor, boolean trustInvalidCertificate) throws Exception {
		super(trustInvalidCertificate);
		this.getInterceptors().add(authInterceptor);
	}
		
}
