package ar.lamansys.sgx.shared.restclient.services;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface RestClientInterface {
    <T> ResponseEntity<T> exchangeGet(String relUrl, Class<T> responseType) throws RestTemplateApiException;

	<T> ResponseEntity<T> exchangeGet(String relUrl, HttpHeaders headers, Class<T> responseType) throws RestTemplateApiException;

	<T> ResponseEntity<T> exchangeDelete(String relUrl, Class<T> responseType) throws RestTemplateApiException;

    <ResponseBody, RequestBody> ResponseEntity<ResponseBody> exchangePost(String relUrl,
                                                                          RequestBody requestBody, Class<ResponseBody> responseType) throws RestTemplateApiException;

    <ResponseBody, RequestBody> ResponseEntity<ResponseBody> exchangePut(String relUrl,
                                                                         RequestBody requestBody, Class<ResponseBody> responseType) throws RestTemplateApiException;

    HttpHeaders getHeaders();

    String getBaseUrl();

    boolean isMocked();
}
