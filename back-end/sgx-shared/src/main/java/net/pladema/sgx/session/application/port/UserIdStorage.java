package net.pladema.sgx.session.application.port;

public interface UserIdStorage {
	/**
	 * Retorna el ID del usuario que hizo el request
	 */
	Integer getUserId();

}
