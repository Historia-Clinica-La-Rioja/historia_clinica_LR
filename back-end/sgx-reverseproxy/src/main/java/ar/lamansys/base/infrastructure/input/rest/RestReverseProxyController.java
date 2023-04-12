package ar.lamansys.base.infrastructure.input.rest;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.base.application.reverseproxyrest.RestReverseProxy;

@RequestMapping
@RestController
@RequiredArgsConstructor
@Slf4j
public class RestReverseProxyController {

	private final RestReverseProxy restReverseProxy;

	@GetMapping("/**")
	@ResponseBody
	public ResponseEntity<?> proxyGet(HttpServletRequest request) {
		return restReverseProxy.run(request);
	}
}
