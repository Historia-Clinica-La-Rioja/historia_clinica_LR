package ar.lamansys.sgx.cubejs.application.dashboardinfo;

import ar.lamansys.sgx.cubejs.domain.DashboardStorage;
import ar.lamansys.sgx.cubejs.domain.DashboardBoInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class DashboardInfoServiceImpl implements DashboardInfoService {

    private final DashboardStorage dashboardStorage;

    public DashboardBoInfo execute(String path, Map<String, String[]> parameterMap) {
        log.debug("Dashboardinfo execute params -> path {}, parameterMap {}", path, parameterMap);
        return dashboardStorage.execute(path, parameterMap);
    }
}
