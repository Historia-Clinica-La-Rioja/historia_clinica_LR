package ar.lamansys.base;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ReverseProxy {
	ResponseEntity<String> getAsString(String path, Map<String, String[]> parameterMap);

	void addHeaders(Map<String, String> headersValues);

    void updateHeader(String tokenHeader, String buildToken);
}
