package ar.lamansys.sgx.auth.user.application.getuseridbytoken;

public interface GetUserIdByToken {

    Integer execute(String token);
}
