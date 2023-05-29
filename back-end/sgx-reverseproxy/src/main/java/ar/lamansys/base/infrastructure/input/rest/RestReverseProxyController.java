package ar.lamansys.base.infrastructure.input.rest;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.base.application.reverseproxyrest.RestReverseProxy;

import java.io.IOException;

@RequestMapping
@RestController
@RequiredArgsConstructor
@Slf4j
public class RestReverseProxyController {

	private final RestReverseProxy restReverseProxy;

	@GetMapping("/**")
	@ResponseBody
	public ResponseEntity<?> proxyGet(HttpServletRequest request) {
		return restReverseProxy.get(request);
	}

	@PostMapping("/**")
	@ResponseBody
	public ResponseEntity<?> proxyPost(HttpServletRequest request) throws IOException {
		return restReverseProxy.post(request);
	}
}
