package net.pladema.security.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
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
import net.pladema.permissions.service.RoleService;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.security.authorization.InstitutionGrantedAuthority;
import net.pladema.security.service.SecurityService;
import net.pladema.security.service.enums.ETokenType;

@Service
public class SecurityServiceImpl implements SecurityService {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityServiceImpl.class);

	private static final String TOKENTYPE = "tokentype";

	private static final String JWT_INVALID = "jwt.invalid";
	
	@Value("${token.secret}")
	private String secret;

	private RoleService roleService;
	
	public SecurityServiceImpl(RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	public Optional<UsernamePasswordAuthenticationToken> getAppAuthentication(String authToken) {
		Optional<Claims> optClaims = Optional.ofNullable(getClaimsFromToken(authToken));
		if (optClaims.isPresent()) {
			Claims claims = optClaims.get();
			LOG.info("{}", claims);
			return Optional.of(new UsernamePasswordAuthenticationToken(getUserId(claims), "", getAuthorities(claims)));
		}
		LOG.info("{}", "Empty claims");
		return Optional.empty();
	}

	@SuppressWarnings("unchecked")
	protected Collection<GrantedAuthority> getAuthorities(Claims claims) {				
		//Los permisos del usuario se obtienen de user_role
		return	roleService.getUserRoleAssignments(getUserId(claims))
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
		Claims claims = getClaimsFromToken(token);
		if (claims.get(TOKENTYPE) == null)
			return false;
		String tokenType = (String) claims.get(TOKENTYPE);
		return tokenType.equals(eTokenType.getUrl());
	}

	@Override
	public Boolean isTokenExpired(String token) {
		return getExpirationDateFromToken(token).map(expDate -> expDate.before(new Date(System.currentTimeMillis())))
				.orElse(false);
	}

	protected Optional<Date> getExpirationDateFromToken(String token) {
		Optional<Claims> optClaims = Optional.ofNullable(getClaimsFromToken(token));
		if (optClaims.isPresent())
			return Optional.ofNullable(optClaims.get().getExpiration());
		return Optional.empty();
	}

	@Override
	public Claims getClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
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

