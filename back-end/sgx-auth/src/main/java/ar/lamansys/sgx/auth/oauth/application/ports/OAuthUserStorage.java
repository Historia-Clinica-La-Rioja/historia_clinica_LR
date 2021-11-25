package ar.lamansys.sgx.auth.oauth.application.ports;

public interface OAuthUserStorage {

    void registerUser(String username, String email, String password);

    void enableUser(String username);

}
