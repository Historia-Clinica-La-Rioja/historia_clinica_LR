package ar.lamansys.pac.application.doreverseproxy;

import javax.servlet.http.HttpServletRequest;

import ar.lamansys.base.ReverseProxy;

import ar.lamansys.base.configuration.HttpClientConfiguration;
import ar.lamansys.base.resttemplate.RestTemplateReverseProxy;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ar.lamansys.pac.PacReverseProxyAutoConfiguration;
import ar.lamansys.pac.domain.PacReverseProxy;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DoReverseProxy {

	private final ReverseProxy reverseProxy;

	public DoReverseProxy(PacReverseProxyAutoConfiguration pacReverseProxyAutoConfiguration, HttpClientConfiguration configuration) throws Exception {
		this.reverseProxy = new RestTemplateReverseProxy(
				pacReverseProxyAutoConfiguration.getServer(),
				configuration.withProxy(pacReverseProxyAutoConfiguration.getProxy()),
				pacReverseProxyAutoConfiguration.getHeaders()
		);
		log.info("Reverse Proxy server enabled to forward URL '{}'", pacReverseProxyAutoConfiguration.getServer());
	}

	public ResponseEntity<?> run(HttpServletRequest request) {
		String path = removeContext(request.getRequestURI(), request.getContextPath());
		log.trace("DoReverseProxy execute params -> path '{}'", path);
		ResponseEntity<?> response = new PacReverseProxy(reverseProxy.getAsString(path, request.getParameterMap())).getResponse();
		log.trace("response from server -> {}", response);
		return response;
	}

	private static String removeContext(String fullURI, String prefix) {
		return fullURI.split(prefix)[1];
	}
}
