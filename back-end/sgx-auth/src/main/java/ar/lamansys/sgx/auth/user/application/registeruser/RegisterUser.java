package ar.lamansys.sgx.auth.user.application.registeruser;

public interface RegisterUser {

    void execute(String username, String email, String password);
}
