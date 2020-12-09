package net.pladema.security.token.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.pladema.security.service.SecurityService;
import net.pladema.security.service.enums.ETokenType;
import net.pladema.security.token.service.TokenService;
import net.pladema.security.token.service.domain.JWToken;
import net.pladema.security.token.service.domain.LocalClaims;
import net.pladema.user.service.UserService;

@Component
public class TokenServiceImpl implements TokenService {

	@Value("${token.secret}")
	private String secret;	
	
	@Value("${token.expiration}")
	private Long tokenExpiration;

	@Value("${refreshToken.expiration}")
	private Long refreshTokenExpiration;

	@Value("${validationToken.expiration}")
	private Long validationToken;

	private final SecurityService securityService;

	private final UserService userService;

	public TokenServiceImpl(SecurityService securityService, UserService userService) {
		super();
		this.securityService = securityService;
		this.userService = userService;
	}

	@Override
	public JWToken generateToken(String username) {
		Integer userId = userService.getUserId(username);
		return newToken(userId);
	}

	@Override
	public JWToken refreshTokens(String refreshToken) {
		Integer userId = securityService.getUserId(refreshToken);

		return newToken(userId);
	}

	private JWToken newToken(Integer userId) {
		String token = createNormalToken(userId);
		String refreshToken = createRefreshToken(userId);

		return new JWToken(token, refreshToken);
	}


	protected String createRefreshToken(Integer userId) {
		Date refreshTokenExpirationDate = generateExpirationDate(refreshTokenExpiration);
		LocalClaims claims = new LocalClaims(ETokenType.REFRESH, userId);
		return tokenToString(claims, refreshTokenExpirationDate);
	}

	protected String createNormalToken(Integer userId) {
		Date tokenExpirationDate = generateExpirationDate(tokenExpiration);
		LocalClaims claims = new LocalClaims(ETokenType.NORMAL, userId);
		return tokenToString(claims, tokenExpirationDate);
	}

	protected String tokenToString(LocalClaims claims, Date expirationDate) {
		return Jwts.builder().setClaims(claims.getClaims()).setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	protected Date generateExpirationDate(Long expiration) {
		return new Date(System.currentTimeMillis() + (expiration * 60000));
	}

	@Override
	public String generateVerificationToken(Integer userId) {
		Date tokenExpirationDate = generateExpirationDate(validationToken);
		LocalClaims claims = new LocalClaims(ETokenType.VERIFICATION, userId);
		return tokenToString(claims, tokenExpirationDate);
	}

	@Override
	public boolean validVerificationToken(String verificationToken) {
		return securityService.validType(verificationToken, ETokenType.VERIFICATION)
				&& !securityService.isTokenExpired(verificationToken);
	}

}
