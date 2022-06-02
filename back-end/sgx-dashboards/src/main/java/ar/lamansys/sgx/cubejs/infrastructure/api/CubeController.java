package ar.lamansys.sgx.cubejs.infrastructure.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.cubejs.application.dashboardinfo.DashboardInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/dashboards/cubejs-api")
@Tag(name = "Dashboards", description = "Dashboards")
public class CubeController {
	private final DashboardInfoService dashboardInfoService;

	@GetMapping("/**")
	@ResponseBody
	public ResponseEntity<String> proxyGet(HttpServletRequest request) {
		String requestContext = request.getContextPath() + "/dashboards/cubejs-api";
		String path = removeContext(request.getRequestURI(), requestContext);

		log.debug("Fetching {}", path);

		return dashboardInfoService.execute(path, request.getParameterMap()).getResponse();
	}

	private static String removeContext(String fullURI, String prefix) {
		return fullURI.split(prefix)[1];
	}

}
