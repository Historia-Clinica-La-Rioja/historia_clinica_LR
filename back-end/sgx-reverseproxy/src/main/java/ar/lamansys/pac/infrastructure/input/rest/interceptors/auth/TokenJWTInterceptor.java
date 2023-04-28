package ar.lamansys.pac.infrastructure.input.rest.interceptors.auth;

import ar.lamansys.pac.infrastructure.input.rest.exceptions.StudyAccessException;
import ar.lamansys.pac.infrastructure.input.rest.exceptions.StudyAccessExceptionEnum;
import ar.lamansys.pac.infrastructure.input.rest.interceptors.AuthInterceptor;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@Slf4j
public class TokenJWTInterceptor extends AbstractAuthInterceptor {

	private final String secret;

	private static final String PREFIX_AUTH = "Bearer";

	private final BiFunction<String, String, Boolean> validator;

	public TokenJWTInterceptor(AuthInterceptor nextAuthInterceptor, String secret) {
		super(nextAuthInterceptor, PREFIX_AUTH, null);
		this.secret = secret;
		this.validator = this::validateToken;
	}

	public TokenJWTInterceptor(AuthInterceptor nextAuthInterceptor, RestTemplate restTemplate) {
		super(nextAuthInterceptor, PREFIX_AUTH, restTemplate);
		this.secret = null;
		this.validator = this::delegateTokenValidationToHSI;
	}

	@PostConstruct
	public void init() {
		log.debug("secret JWT '{}'", secret);
	}

	@Override
	public boolean process(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String authorizationHeader = super.getAuthorizationHeader(request);
		if (authorizationHeader == null)
			return nextAuthInterceptor.process(request, response);

		String token = authorizationHeader.substring(7);

		return this.validator.apply(token, getStudyInstanceUIDFromContext(request));
	}

	private boolean delegateTokenValidationToHSI(String token, String studyInstanceUIDFromContext) {
		return super.checkWithHSI(token, studyInstanceUIDFromContext)
				.orElseThrow(() -> new StudyAccessException(StudyAccessExceptionEnum.UNAUTHORIZED, StudyAccessExceptionEnum.UNAUTHORIZED.getMessage())) != null;
	}

	private boolean validateToken(String token, String studyInstanceUIDFromContext) {
		String studyInstanceUIDFromToken = this.ownParseToken(token);
		return super.hasTokenStudyPermissions(studyInstanceUIDFromToken, studyInstanceUIDFromContext);
	}

	private String ownParseToken(String token) throws StudyAccessException {
		log.trace("Parsing token '{}' with secret '{}'", token, secret);
		return parseClaims(token, secret)
				.map(claims -> {
					if (!claims.containsKey("studyInstanceUID"))
						throw new StudyAccessException(StudyAccessExceptionEnum.MALFORMED, StudyAccessExceptionEnum.MALFORMED.getMessage());
					return (String) claims.get("studyInstanceUID");
				})
				.orElseThrow(() -> new StudyAccessException(StudyAccessExceptionEnum.UNAUTHORIZED, StudyAccessExceptionEnum.UNAUTHORIZED.getMessage()));
	}

	private static Optional<Map<String, Object>> parseClaims(String token, String secret) {
		try {
			Map<String, Object> claims = Jwts.parser()
					.setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
					.parseClaimsJws(token)
					.getBody();
			Assert.notNull(claims, "Token inválido");
			return Optional.of(claims);
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}

