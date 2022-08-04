package net.pladema.sgh.app.security.infraestructure.filters;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@WebFilter("/*")
public class StatsFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// empty
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		Instant start = Instant.now();
		try {
			chain.doFilter(req, resp);
		} finally {
			Instant finish = Instant.now();
			long time = Duration.between(start, finish).toMillis();
			if (time > 3000) {
				log.warn("{}: {} ms ", ((HttpServletRequest) req).getRequestURI(),  time);
			}
		}
	}

	@Override
	public void destroy() {
		// empty
	}
}