package ar.lamansys.pac.infrastructure.input.rest;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.pac.application.doreverseproxy.DoReverseProxy;
import lombok.RequiredArgsConstructor;

@RequestMapping
@RequiredArgsConstructor
@RestController
@Slf4j
public class PacReverseProxyController {

	private final DoReverseProxy doReverseProxy;

	@GetMapping("/**")
	@ResponseBody
	public ResponseEntity<?> proxyGet(HttpServletRequest request) {
		return doReverseProxy.run(request);
	}
}
