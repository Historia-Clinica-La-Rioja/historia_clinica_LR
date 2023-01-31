package ar.lamansys.sgx.auth.jwt.infrastructure.input.service;

import ar.lamansys.sgx.auth.jwt.application.generatetoken.GenerateToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenExternalService {
	private final GenerateToken generateToken;

	public String generateToken(Integer userId, String username) {
		log.debug("Generate token for user {}", username);
		return generateToken.generateToken(userId, username);
	}
}
