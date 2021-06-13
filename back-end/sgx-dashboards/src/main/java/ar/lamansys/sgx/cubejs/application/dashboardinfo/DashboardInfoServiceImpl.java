package ar.lamansys.sgx.cubejs.application.dashboardinfo;

import ar.lamansys.sgx.cubejs.domain.DashboardStorage;
import ar.lamansys.sgx.cubejs.domain.DashboardBoInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class DashboardInfoServiceImpl implements DashboardInfoService {

    private final DashboardStorage dashboardStorage;
    private final Logger logger;

    public DashboardInfoServiceImpl(DashboardStorage dashboardStorage) {
        this.dashboardStorage = dashboardStorage;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    public DashboardBoInfo execute(String path, Map<String, String[]> parameterMap) {
        logger.debug("Dashboardinfo execute params -> path {}, parameterMap {}", path, parameterMap);
        return dashboardStorage.execute(path, parameterMap);
    }
}
