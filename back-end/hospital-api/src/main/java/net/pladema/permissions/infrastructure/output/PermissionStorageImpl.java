package net.pladema.permissions.infrastructure.output;

import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.JwtExternalService;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.application.ports.PermissionStorage;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PermissionStorageImpl implements PermissionStorage {

	private final JwtExternalService jwtExternalService;

	public PermissionStorageImpl(JwtExternalService jwtExternalService) {
		this.jwtExternalService = jwtExternalService;
	}
	@Override
	public Optional<Integer> fetchUserIdFromToken(String token) {
		return jwtExternalService.fetchUserIdFromNormalToken(token);
	}
}
