package ar.lamansys.sgx.auth.user.application.updateusername;

public interface UpdateUsername {

    void execute(Integer userId, String username);
}