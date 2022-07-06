package ar.lamansys.sgx.auth.jwt.infrastructure.input.service;

import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.JWTokenDto;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.LoginDto;

import java.util.Optional;

public interface JwtExternalService {

    JWTokenDto login(LoginDto login) throws BadLoginException;

	Optional<Integer> fetchUserIdFromNormalToken(String token);
}
