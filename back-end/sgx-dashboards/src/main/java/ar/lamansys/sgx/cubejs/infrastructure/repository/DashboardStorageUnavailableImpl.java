package ar.lamansys.sgx.cubejs.infrastructure.repository;

import ar.lamansys.sgx.cubejs.application.dashboardinfo.excepciones.DashboardInfoExceptionEnum;
import ar.lamansys.sgx.cubejs.domain.DashboardStorage;
import ar.lamansys.sgx.cubejs.domain.DashboardBoInfo;

import java.util.Map;

public class DashboardStorageUnavailableImpl implements DashboardStorage {

    @Override
    public DashboardBoInfo execute(String path, Map<String, String[]> parameterMap) {
        throw DashboardInfoExceptionEnum.API_URL_NOT_DEFINED.getException();
    }
}
