package ar.lamansys.pac.infrastructure.input.rest.interceptors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "api-key")
@Order(1)
public class ApiKeyInterceptor implements HandlerInterceptor {

	@NonNull
	private String header;
	@NonNull
	private String secret;

	@PostConstruct
	public void init() {
		log.debug("apiKey header {}", header);
		log.debug("apiKey secret {}", secret);
	}

	@Override
	public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
		try {
			String apiKey = request.getHeader(header);
			Assert.isTrue(apiKey.equals(secret), "");
		} catch (Exception e) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Acceso no autorizado, facilite las credenciales pertinentes");
		}
		return true;
	}

	@Override
	public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {
		// pass
	}

	@Override
	public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
		// pass
	}
}

