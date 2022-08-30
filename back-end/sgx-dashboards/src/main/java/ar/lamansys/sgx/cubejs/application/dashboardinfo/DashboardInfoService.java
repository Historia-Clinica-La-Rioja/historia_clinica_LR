package ar.lamansys.sgx.cubejs.application.dashboardinfo;

import ar.lamansys.sgx.cubejs.domain.DashboardBoInfo;

import java.util.Map;

public interface DashboardInfoService {

    DashboardBoInfo execute(String path, Map<String, String[]> parameterMap);

}
