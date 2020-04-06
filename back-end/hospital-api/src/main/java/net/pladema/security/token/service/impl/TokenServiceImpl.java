package net.pladema.security.token.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.pladema.permissions.service.RoleService;
import net.pladema.security.service.SecurityService;
import net.pladema.security.service.enums.ETokenType;
import net.pladema.security.token.service.TokenService;
import net.pladema.security.token.service.domain.JWToken;
import net.pladema.security.token.service.domain.LocalClaims;
import net.pladema.security.token.service.domain.Login;
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

	private final RoleService roleService;

	private final UserService userService;

	public TokenServiceImpl(SecurityService securityService, RoleService roleService, UserService userService) {
		super();
		this.securityService = securityService;
		this.roleService = roleService;
		this.userService = userService;
	}

	@Override
	public JWToken generateToken(Login login) {
		String username = login.getUsername();

		String token = createNormalToken(username);
		String refreshToken = createRefreshToken(username);

		JWToken result = new JWToken();
		result.setToken(token);
		result.setRefreshToken(refreshToken);
		return result;
	}

	protected String createRefreshToken(String username) {
		Integer userId = userService.getUserId(username);
		Date refreshTokenExpirationDate = generateExpirationDate(refreshTokenExpiration);
		LocalClaims claims = new LocalClaims(ETokenType.REFRESH, userId, roleService.getAuthoritiesClaims(userId));
		return tokenToString(claims, refreshTokenExpirationDate);
	}

	protected String createNormalToken(String username) {
		Integer userId = userService.getUserId(username);
		Date tokenExpirationDate = generateExpirationDate(tokenExpiration);
		LocalClaims claims = new LocalClaims(ETokenType.NORMAL, userId, roleService.getAuthoritiesClaims(userId));
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
