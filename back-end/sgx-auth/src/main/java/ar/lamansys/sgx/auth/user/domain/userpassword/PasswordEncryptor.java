package ar.lamansys.sgx.auth.user.domain.userpassword;

public interface PasswordEncryptor {

    String encode(String rawPassword, String salt, String hashAlgorithm);

    boolean matches(String password, String password1);
}
