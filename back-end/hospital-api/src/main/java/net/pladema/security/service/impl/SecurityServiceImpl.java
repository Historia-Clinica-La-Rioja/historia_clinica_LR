package net.pladema.security.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.security.authorization.InstitutionGrantedAuthority;
import net.pladema.security.service.SecurityService;
import net.pladema.security.service.enums.ETokenType;

@Service
public class SecurityServiceImpl implements SecurityService {


	private static final String TOKENTYPE = "tokentype";

	private static final String JWT_INVALID = "jwt.invalid";

	private final Logger logger;
	private final String secret;
	private final UserAssignmentService userAssignmentService;
	
	public SecurityServiceImpl(
			@Value("${token.secret}") String secret,
			UserAssignmentService userAssignmentService) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.secret = secret;
		this.userAssignmentService = userAssignmentService;
	}

	@Override
	public Optional<UsernamePasswordAuthenticationToken> getAppAuthentication(String authToken) {
		Optional<UsernamePasswordAuthenticationToken> userAuthToken = parseClaimsFromToken(authToken)
				.map((Claims claims) -> {
					logger.debug("Claims {}", claims);
					return new UsernamePasswordAuthenticationToken(getUserId(claims), "", getAuthorities(claims));
				});
		logger.debug("Token {}", userAuthToken);
		return userAuthToken;
	}

	@SuppressWarnings("unchecked")
	protected Collection<GrantedAuthority> getAuthorities(Claims claims) {				
		//Los permisos del usuario se obtienen de user_role
		return	userAssignmentService.getRoleAssignment(getUserId(claims))
				.stream()
				.map(InstitutionGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public boolean validateCredentials(String token) {
		return !isTokenExpired(token) && validType(token, ETokenType.NORMAL);
	}

	@Override
	public boolean validType(String token, ETokenType eTokenType) {
		Object tokenType = parseClaimsFromToken(token)
				.map(claims -> claims.get(TOKENTYPE))
				.orElse(null);
		return eTokenType.getUrl().equals(tokenType);
	}

	@Override
	public Boolean isTokenExpired(String token) {
		return getExpirationDateFromToken(token).map(expDate -> expDate.before(new Date(System.currentTimeMillis())))
				.orElse(false);
	}

	protected Optional<Date> getExpirationDateFromToken(String token) {
		return parseClaimsFromToken(token).map(Claims::getExpiration);
	}

	@Override
	public Claims getClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	protected Optional<Claims> parseClaimsFromToken(String token) {
		try {
			return Optional.ofNullable(Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody());
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.debug(e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public Integer getUserId(String token) {
		return getUserId(getClaimsFromToken(token));
	}

	protected Integer getUserId(Claims claims) {
		return (Integer) Optional.ofNullable(claims.get("userId"))
				.orElseThrow(() -> new BadCredentialsException(JWT_INVALID));
	}

	@Override
	public boolean validResetPassword(Integer userId, String verificationToken) {
		Integer userIdVT = getUserId(verificationToken);
		return userIdVT.equals(userId);
	}

}

