package ar.lamansys.sgx.cubejs.infrastructure.api;

import ar.lamansys.sgx.cubejs.application.dashboardinfo.DashboardInfoService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dashboards/cubejs-api")
@Api(value = "Dashboards", tags = { "Dashboards" })
public class CubeController {
	private final Logger logger;
	private final DashboardInfoService dashboardInfoService;

	public CubeController(
			DashboardInfoService dashboardInfoService){
		this.dashboardInfoService = dashboardInfoService;
		this.logger = LoggerFactory.getLogger(getClass());
	}

	@GetMapping("/**")
	@ResponseBody
	public ResponseEntity<String> proxyGet(HttpServletRequest request) {
		String requestContext = request.getContextPath() + "/dashboards/cubejs-api";
		String path = removeContext(request.getRequestURI(), requestContext);

		logger.debug("Fetching {}", path);

		return dashboardInfoService.execute(path, request.getParameterMap()).getResponse();
	}

	private static String removeContext(String fullURI, String prefix) {
		return fullURI.split(prefix)[1];
	}



}
