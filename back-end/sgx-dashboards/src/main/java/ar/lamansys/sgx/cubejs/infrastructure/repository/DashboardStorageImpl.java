package ar.lamansys.sgx.cubejs.infrastructure.repository;

import ar.lamansys.sgx.cubejs.domain.DashboardStorage;
import ar.lamansys.sgx.cubejs.infrastructure.configuration.CubejsAutoConfiguration;
import ar.lamansys.sgx.cubejs.domain.DashboardBoInfo;
import ar.lamansys.sgx.shared.proxy.reverse.ReverseProxy;
import ar.lamansys.sgx.shared.proxy.reverse.resttemplate.RestTemplateReverseProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DashboardStorageImpl implements DashboardStorage {

    private final ReverseProxy reverseProxy;
    private final Logger logger;

    public DashboardStorageImpl(
            CubejsAutoConfiguration cubejsAutoConfiguration) throws Exception {
        this.reverseProxy = new RestTemplateReverseProxy(cubejsAutoConfiguration.getApiUrl(), cubejsAutoConfiguration.getHeaders());
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public DashboardBoInfo execute(String path, Map<String, String[]> parameterMap) {
        logger.debug("DashboardStorage execute params -> path {}, parameterMap {}", path, parameterMap);
        return new DashboardBoInfo(reverseProxy.getAsString(path, parameterMap));
    }
}
