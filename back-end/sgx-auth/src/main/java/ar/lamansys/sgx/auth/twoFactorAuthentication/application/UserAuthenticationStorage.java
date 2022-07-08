package ar.lamansys.sgx.auth.twoFactorAuthentication.application;

import java.util.Optional;

public interface UserAuthenticationStorage {

	String getUsername(Integer userId);

	void setTwoFactorAuthenticationSecret(Integer userId, String secret);

	Optional<String> getTwoFactorAuthenticationSecret(Integer userId);

	void enableTwoFactorAuthentication(Integer userId);

	Boolean userHasTwoFactorAuthenticationEnabled(Integer userId);

	String generateSecretKey();

}
