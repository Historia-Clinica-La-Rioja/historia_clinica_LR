package ar.lamansys.sgx.auth.twoWayEncryption.infrastructure.output;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwoWayEncryptionService extends AbstractTwoWayEncryption {

	public TwoWayEncryptionService(@Value("${auth.2we.password:}") String password) {
		super(password);
	}
}
