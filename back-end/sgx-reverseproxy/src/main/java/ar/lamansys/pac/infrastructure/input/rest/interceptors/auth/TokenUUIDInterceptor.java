package ar.lamansys.pac.infrastructure.input.rest.interceptors.auth;

import ar.lamansys.pac.domain.StudyPermissionBo;
import ar.lamansys.pac.infrastructure.input.rest.exceptions.StudyAccessException;
import ar.lamansys.pac.infrastructure.input.rest.exceptions.StudyAccessExceptionEnum;
import ar.lamansys.pac.infrastructure.input.rest.interceptors.AuthInterceptor;
import ar.lamansys.pac.infrastructure.output.inmemory.repository.StudyPermissionsUUIDRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
public class TokenUUIDInterceptor extends AbstractAuthInterceptor {

    private static final String PREFIX_AUTH = "UUID";
    private final StudyPermissionsUUIDRepository tokenUUIDRepository;

    public TokenUUIDInterceptor(AuthInterceptor nextAuthInterceptor, StudyPermissionsUUIDRepository tokenUUIDRepository, RestTemplate restTemplate) {
        super(nextAuthInterceptor, PREFIX_AUTH, restTemplate);
        this.tokenUUIDRepository = tokenUUIDRepository;
    }

    @Override
    public boolean process(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String authorizationHeader = super.getAuthorizationHeader(request);
        if (authorizationHeader == null)
            return nextAuthInterceptor.process(request, response);

        String tokenFromRequest = authorizationHeader.substring(5);
        String studyInstanceUIDFromContext = AbstractAuthInterceptor.getStudyInstanceUIDFromContext(request);
        return this.getTokenInCache(tokenFromRequest, studyInstanceUIDFromContext)
                .or(() -> checkWithHSI(tokenFromRequest, studyInstanceUIDFromContext)
                        .map((tokenUUIDRepository::saveStudyPermission)))
                .orElseThrow(() -> new StudyAccessException(StudyAccessExceptionEnum.UNAUTHORIZED, StudyAccessExceptionEnum.UNAUTHORIZED.getMessage())) != null;
    }

    private Optional<StudyPermissionBo> getTokenInCache(String tokenStudy, String studyInstanceUIDFromContext) {
        Optional<StudyPermissionBo> permission = tokenUUIDRepository.getStudyPermission(tokenStudy)
                .filter(studyPermissionBo -> super.hasTokenStudyPermissions(studyPermissionBo.getStudyInstanceUID(), studyInstanceUIDFromContext));
        if (permission.isPresent() && permission.get().expired()) {
                tokenUUIDRepository.removeStudyPermission(permission.get());
                return Optional.empty();
        }
        return permission;

    }
}
