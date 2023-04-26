package ar.lamansys.pac.application.generatetoken;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.lamansys.pac.domain.jwt.JWTUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class GenerateToken {

	@Value("${token.secret}")
	private String secret;
	@Value("${token.expiration}")
	private Duration tokenExpiration;

	public String run(String studyInstanceUID) {
		Map<String, Object> claims = Map.of(
				"studyInstanceUID", studyInstanceUID
		);
		return JWTUtils.generate(claims, secret, tokenExpiration);
	}

	@PostConstruct
	public void init() {
		log.debug("GenerateToken -> secret '{}' expiration '{}'", secret, tokenExpiration);
	}
}
