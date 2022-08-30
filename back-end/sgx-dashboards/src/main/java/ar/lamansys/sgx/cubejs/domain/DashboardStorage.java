package ar.lamansys.sgx.cubejs.domain;


import java.util.Map;

public interface DashboardStorage {

    DashboardBoInfo execute(String path, Map<String, String[]> parameterMap);

}
