package ar.lamansys.pac;

import ar.lamansys.pac.infrastructure.input.rest.interceptors.ApiKeyInterceptor;
import ar.lamansys.pac.infrastructure.input.rest.interceptors.TokenInterceptor;
import lombok.AllArgsConstructor;

import lombok.NonNull;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@AllArgsConstructor
public class PacMvcConfig implements WebMvcConfigurer {

	private final ApiKeyInterceptor apiKeyInterceptor;
	private final TokenInterceptor tokenInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(apiKeyInterceptor).addPathPatterns("/token/**");
		registry.addInterceptor(tokenInterceptor).addPathPatterns("/dicom-web/studies/**", "/wado/**");
		// Interceptor anónimo para denegar cualquier otro request
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}
		}).excludePathPatterns("/token/**", "/dicom-web/studies/**", "/wado/**");
	}
}
