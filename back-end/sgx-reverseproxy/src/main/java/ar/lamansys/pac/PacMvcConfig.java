package ar.lamansys.pac;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@Getter
@AllArgsConstructor
@Slf4j
public class PacMvcConfig implements WebMvcConfigurer {
	private final HandlerInterceptor authChain;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authChain).addPathPatterns("/dicom-web/studies/**", "/wado/**");
		// Interceptor anónimo para denegar cualquier otro request
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}
		}).excludePathPatterns("/dicom-web/studies/**", "/wado/**");
	}
}
