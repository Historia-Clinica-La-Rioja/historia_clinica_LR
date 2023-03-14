package ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.output;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.CypherStorage;
import ar.lamansys.sgx.auth.twoWayEncryption.infrastructure.output.AbstractTwoWayEncryption;

@Service
public class CypherStorageImpl extends AbstractTwoWayEncryption implements CypherStorage {

	public CypherStorageImpl(@Value("${auth.2fa.password:}") String password) {
		super(password);
	}
}
