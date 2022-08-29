package ar.lamansys.sgx.cubejs.infrastructure.repository;

import java.time.Duration;
import java.util.Map;

import org.slf4j.LoggerFactory;

import ar.lamansys.sgx.cubejs.CubejsAutoConfiguration;
import ar.lamansys.sgx.cubejs.domain.DashboardBoInfo;
import ar.lamansys.sgx.cubejs.domain.DashboardStorage;
import ar.lamansys.sgx.cubejs.infrastructure.repository.permissions.UserPermissionStorage;
import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import ar.lamansys.sgx.shared.proxy.reverse.ReverseProxy;
import ar.lamansys.sgx.shared.proxy.reverse.resttemplate.RestTemplateReverseProxy;
import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.token.JWTUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DashboardStorageImpl implements DashboardStorage {

    private final ReverseProxy reverseProxy;

	private final UserPermissionStorage userPermissionStorage;

	private final String secret;

	private final String cubeTokenHeader;

	private final Duration tokenExpiration;

    public DashboardStorageImpl(
			CubejsAutoConfiguration cubejsAutoConfiguration,
			UserPermissionStorage userPermissionStorage,
			HttpClientConfiguration configuration,
			String secret,
			String cubeTokenHeader,
			Duration tokenExpiration
	) throws Exception {
        this.reverseProxy = new RestTemplateReverseProxy(
				cubejsAutoConfiguration.getApiUrl(),
				configuration,
				cubejsAutoConfiguration.getHeaders()
		);
		this.userPermissionStorage = userPermissionStorage;
		this.secret = secret;
		this.tokenExpiration = tokenExpiration;
		this.cubeTokenHeader = cubeTokenHeader;
        log.info("DashboardStorage cubejs enabled in URL '{}'", cubejsAutoConfiguration.getApiUrl());
    }

    @Override
    public DashboardBoInfo execute(String path, Map<String, String[]> parameterMap) {
        log.debug("DashboardStorage execute params -> path {}, parameterMap {}", path, parameterMap);
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
