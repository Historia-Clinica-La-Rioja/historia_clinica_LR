package ar.lamansys.sgx.auth.jwt.application.cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CookieService {

	private final String contextPath;

	private final String authDomain;

	private final boolean secure;

	public CookieService(
			@Value("${server.servlet.context-path}") String contextPath,
			@Value("${app.auth.domain}") String authDomain,
			@Value("${app.auth.secure}") boolean secure
	) {
		this.contextPath = contextPath;
		this.authDomain = authDomain;
		this.secure = secure;
	}

	public String tokenCookieHeader(String token) {
		return ResponseCookie.from("token", token)
				.httpOnly(true)
				.secure(secure)
				.path(contextPath)
				.domain(authDomain)
				.build()
				.toString();
	}

	public String refreshTokenCookieHeader(String refreshToken) {
		return ResponseCookie.from("refreshToken", refreshToken)
			.httpOnly(true)
			.secure(secure)
			.path(contextPath + "/auth/refresh")
			.domain(authDomain)
			.build()
			.toString();
	}

	public String deleteTokenCookieHeader(boolean refreshType) {
		return ResponseCookie.from(refreshType ? "refreshToken" : "token", null)
			.httpOnly(true)
			.secure(secure)
			.path(contextPath + (refreshType ? "/auth/refresh" : ""))
			.domain(authDomain)
			.maxAge(0)
			.build()
			.toString();
	}

	public ResponseEntity.BodyBuilder deleteTokensResponse(HttpStatus status) {
		return ResponseEntity.status(status)
			.header(HttpHeaders.SET_COOKIE, deleteTokenCookieHeader(false))
			.header(HttpHeaders.SET_COOKIE, deleteTokenCookieHeader(true));
	}

}
