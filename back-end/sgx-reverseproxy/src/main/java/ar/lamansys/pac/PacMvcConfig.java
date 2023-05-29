package ar.lamansys.pac;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Configuration
@Getter
@AllArgsConstructor
@Slf4j
public class PacMvcConfig implements WebMvcConfigurer {
	private final HandlerInterceptor authChain;

	@Value("${app.imagenetwork.allowed.endpoints}")
	private final String[] imageNetworkPathPattern;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		Arrays.stream(imageNetworkPathPattern).forEach(pattern ->
				registry.addInterceptor(authChain).addPathPatterns(pattern));
		// Interceptor anónimo para denegar cualquier otro request
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}
		}).excludePathPatterns(imageNetworkPathPattern);
	}

	@PostConstruct
	void started() {
		log.debug("Image Network allow patterns paths {}", Arrays.deepToString(imageNetworkPathPattern));
	}
}
