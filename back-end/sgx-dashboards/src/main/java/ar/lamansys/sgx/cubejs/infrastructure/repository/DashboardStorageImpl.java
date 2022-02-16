package ar.lamansys.sgx.cubejs.infrastructure.repository;

import ar.lamansys.sgx.cubejs.domain.DashboardStorage;
import ar.lamansys.sgx.cubejs.infrastructure.configuration.CubejsAutoConfiguration;
import ar.lamansys.sgx.cubejs.domain.DashboardBoInfo;
import ar.lamansys.sgx.cubejs.infrastructure.repository.permissions.UserPermissionStorage;
import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import ar.lamansys.sgx.shared.proxy.reverse.ReverseProxy;
import ar.lamansys.sgx.shared.proxy.reverse.resttemplate.RestTemplateReverseProxy;
import ar.lamansys.sgx.shared.token.JWTUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;


public class DashboardStorageImpl implements DashboardStorage {

    private final ReverseProxy reverseProxy;

    private final Logger logger;

	private final UserPermissionStorage userPermissionStorage;

	private final String secret;

	private final String cubeTokenHeader;

	private final Duration tokenExpiration;

    public DashboardStorageImpl(CubejsAutoConfiguration cubejsAutoConfiguration,
								UserPermissionStorage userPermissionStorage,
								String secret,
								String cubeTokenHeader,
								Duration tokenExpiration) throws Exception {
        this.reverseProxy = new RestTemplateReverseProxy(cubejsAutoConfiguration.getApiUrl(), cubejsAutoConfiguration.getHeaders());
		this.userPermissionStorage = userPermissionStorage;
		this.secret = secret;
		this.tokenExpiration = tokenExpiration;
		this.cubeTokenHeader = cubeTokenHeader;
		this.logger = LoggerFactory.getLogger(getClass());
        logger.info("DashboardStorage cubejs enabled in URL '{}'", cubejsAutoConfiguration.getApiUrl());
    }

    @Override
    public DashboardBoInfo execute(String path, Map<String, String[]> parameterMap) {
        logger.debug("DashboardStorage execute params -> path {}, parameterMap {}", path, parameterMap);
		reverseProxy.updateHeader(cubeTokenHeader, buildToken());
        return new DashboardBoInfo(reverseProxy.getAsString(path, parameterMap));
    }

	private String buildToken() {
		var user = SecurityContextUtils.getUserDetails();
		var permissions = userPermissionStorage.fetchPermissionInfoByUserId(user.userId);

		Map<String, Object> claims = Map.of(
				"userId", user.userId,
				"roles", permissions
		);
		return JWTUtils.generate(claims, user.getUsername(), secret, tokenExpiration);
	}


}
