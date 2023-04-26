package ar.lamansys.pac.infrastructure.input.rest.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.lamansys.pac.application.exception.PacException;
import ar.lamansys.pac.application.exception.PacExceptionEnum;
import ar.lamansys.pac.application.parsetoken.ParseToken;
import ar.lamansys.pac.domain.StudyPermissionDataBo;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static ar.lamansys.base.application.reverseproxyrest.configuration.RestUtils.removeContext;

@Slf4j
@AllArgsConstructor
@Component
@Order(2)
public class TokenInterceptor implements HandlerInterceptor {

	private final ParseToken parseToken;

	@Override
	public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
		if ("OPTIONS".equals(request.getMethod()) && request.getHeader("Access-Control-Request-Method") != null)
			return true;

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
			throw new PacException(PacExceptionEnum.MALFORMED, PacExceptionEnum.MALFORMED.getMessage());

		log.trace("Authorization: '{}'", authorizationHeader);
		String token = authorizationHeader.substring(7);
		StudyPermissionDataBo studyPermissionDataBo = parseToken.run(token);
		String studyInstanceUIDFromRequest = getStudyInstanceUIDFromContext(request);

		if (!studyPermissionDataBo.getStudyInstanceUID().equals(studyInstanceUIDFromRequest)) {
			log.debug("AUTHORIZATION FAILED: '{}' != '{}'", studyPermissionDataBo.getStudyInstanceUID(), studyInstanceUIDFromRequest);
			throw new PacException(PacExceptionEnum.UNAUTHORIZED, PacExceptionEnum.UNAUTHORIZED.getMessage());
		}

		return true;
	}

	@Override
	public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {
		// pass
	}

	@Override
	public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
		// pass
	}

	private String getStudyInstanceUIDFromContext(HttpServletRequest request) {
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
}

