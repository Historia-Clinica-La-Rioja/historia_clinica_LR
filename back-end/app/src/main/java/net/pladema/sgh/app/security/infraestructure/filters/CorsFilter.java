package net.pladema.sgh.app.security.infraestructure.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.actuator.infrastructure.configuration.AppNode;
import ar.lamansys.sgx.shared.stats.TimeProfilingUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CorsFilter implements Filter {

	private final String tokenHeader;
	private final AppNode appNode;

	public CorsFilter(
			@Value("${token.header}") String tokenHeader,
			AppNode appNode
	) {
		this.tokenHeader = tokenHeader;
		this.appNode = appNode;
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		String requestURI = ((HttpServletRequest) req).getRequestURI();
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Accept-Language,  " + tokenHeader);
		response.setHeader("App-Node", appNode.nodeId);

		var entpointStat = TimeProfilingUtil.start("Endpoint");
		chain.doFilter(req, res);
		entpointStat.done(requestURI);
	}

	@Override
	public void init(FilterConfig filterConfig) {
		// nothing to do
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}
