package ar.lamansys.sgx.auth.twoFactorAuthentication.application;

public interface CypherStorage {

	String encrypt(String input);

	String decrypt(String input);

}
