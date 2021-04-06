package ar.lamansys.sgx.proxy.reverse;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface ReverseProxy {
	ResponseEntity<String> getAsString(String path, Map<String, String[]> parameterMap);
}
