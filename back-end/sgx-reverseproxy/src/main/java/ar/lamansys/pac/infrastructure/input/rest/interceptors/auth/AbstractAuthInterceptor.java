package ar.lamansys.pac.infrastructure.input.rest.interceptors.auth;

import static ar.lamansys.base.application.reverseproxyrest.configuration.RestUtils.removeContext;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ar.lamansys.pac.domain.StudyPermissionBo;
import ar.lamansys.pac.infrastructure.input.rest.dto.TokenDto;
import ar.lamansys.pac.infrastructure.input.rest.exceptions.StudyAccessException;
import ar.lamansys.pac.infrastructure.input.rest.exceptions.StudyAccessExceptionEnum;
import ar.lamansys.pac.infrastructure.input.rest.interceptors.AuthInterceptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractAuthInterceptor implements AuthInterceptor {

    protected final AuthInterceptor nextAuthInterceptor;
    protected final String prefixAuth;
    private static final String API_CONTEXT = "api/public-api";
    private static final String IMAGENETWORK_PATH = "imagenetwork";
    private static final String PERMISSION_CHECK_PATH = "permission/check";

    @Value("${app.hsi.url}")
    private String url;
	@Value("${app.hsi.api-key.header}")
	private String apiKeyHeader;
	@Value("${app.hsi.api-key.value}")
	private String apiKey;
    @Value("${app.imagenetwork.token.expiration}")
    private Duration tokenExpiration;
    private final RestTemplate restTemplate;

    protected AbstractAuthInterceptor(AuthInterceptor nextAuthInterceptor, String prefixAuth, RestTemplate restTemplate) {
        this.nextAuthInterceptor = nextAuthInterceptor;
        this.prefixAuth = prefixAuth + " ";
        this.restTemplate = restTemplate;
    }

    public boolean hasTokenStudyPermissions(String studyInstanceUIDFromToken, String studyInstanceUIDFromContext) {
        if (!studyInstanceUIDFromToken.equals(studyInstanceUIDFromContext)) {
            log.debug("AUTHORIZATION FAILED: '{}' != '{}'", studyInstanceUIDFromToken, studyInstanceUIDFromContext);
            throw new StudyAccessException(StudyAccessExceptionEnum.UNAUTHORIZED, StudyAccessExceptionEnum.UNAUTHORIZED.getMessage());
        }
        return true;
    }

    protected String getAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null)
            throw new StudyAccessException(StudyAccessExceptionEnum.MALFORMED, StudyAccessExceptionEnum.MALFORMED.getMessage());
        if (authorizationHeader.startsWith(prefixAuth)) {
            log.trace("Authorization: '{}'", authorizationHeader);
            return authorizationHeader;
        }
        return null;
    }

    protected static String getStudyInstanceUIDFromContext(HttpServletRequest request) {
        String context = removeContext(request.getRequestURI(), request.getContextPath());
        log.trace("studyInstanceUID from context '{}'", context);
        String dicomProtocol = context.split("/")[1];
        switch (dicomProtocol) {
            case "dicom-web": {
                // /dicom-web/<resources>/<studyInstanceUID>
                return context.split("/")[3];
            }
            case "wado": {
                // /wado?studyUID=<id>
                return request.getParameter("studyUID");
            }
            default:
                throw new UnsupportedOperationException();
        }
    }

    protected Optional<StudyPermissionBo> checkWithHSI(String tokenStudy, String studyInstanceUID) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(this.url)
                .path(String.format("%s/%s/%s/%s", API_CONTEXT, IMAGENETWORK_PATH, studyInstanceUID, PERMISSION_CHECK_PATH))
                .queryParam("token", tokenStudy);

        URI uri = uriBuilder.build().toUri();
        log.trace("URI to check token: '{}'", uri);
		HttpHeaders headers = new HttpHeaders();
		headers.set(apiKeyHeader, apiKey);
        ResponseEntity<TokenDto> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), TokenDto.class);

        if (response.getStatusCode() != HttpStatus.OK)
            return Optional.empty();

        StudyPermissionBo studyPermissionBo = new StudyPermissionBo(Objects.requireNonNull(response.getBody()).getToken(), studyInstanceUID, tokenExpiration);

        // HSI returned 200 --> the token was registered to access that study instance
        return Optional.of(studyPermissionBo);
    }
}
