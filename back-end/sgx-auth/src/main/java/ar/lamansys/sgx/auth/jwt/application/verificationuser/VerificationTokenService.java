package ar.lamansys.sgx.auth.jwt.application.verificationuser;

public interface VerificationTokenService {
	String generateVerificationToken(Integer userId, String username);
	boolean validVerificationToken(String verificationToken);
}
