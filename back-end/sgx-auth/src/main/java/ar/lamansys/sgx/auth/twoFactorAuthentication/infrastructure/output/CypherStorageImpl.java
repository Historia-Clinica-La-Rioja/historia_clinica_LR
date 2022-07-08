package ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.output;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.CypherStorage;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
@Slf4j
public class CypherStorageImpl implements CypherStorage {

	private static final String SALT = "salt";
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	private SecretKey secretKey;
	private IvParameterSpec iv;

	public CypherStorageImpl(@Value("${auth.2fa.password:}") String password) {
		this.secretKey = this.generateEncryptionKey(password, SALT);
		this.iv = this.generateIv();
	}

	private SecretKey generateEncryptionKey(String password, String salt) {
		SecretKey secret = null;
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65353, 256);
			secret = new SecretKeySpec(factory.generateSecret(spec)
					.getEncoded(), "AES");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			log.error(e.getMessage());
		}
		return secret;
	}

	private IvParameterSpec generateIv() {
		byte[] iv = new byte[16];
		for (int i = 0; i < 16; i++) {
			iv[i] = (byte) i;
		}
		return new IvParameterSpec(iv);
	}

	public String encrypt(String input) {
		byte[] cypherText = new byte[0];
		try {
			Cipher cypher = Cipher.getInstance(ALGORITHM);
			cypher.init(Cipher.ENCRYPT_MODE, this.secretKey, this.iv);
			cypherText = cypher.doFinal(input.getBytes());
		} catch (Exception e)  {
			log.error(e.getMessage());
		}
		return Base64.getEncoder()
				.encodeToString(cypherText);
	}

	public String decrypt(String cypherText){
		byte[] plainText = new byte[0];
		try {
			Cipher cypher = Cipher.getInstance(ALGORITHM);
			cypher.init(Cipher.DECRYPT_MODE, this.secretKey, this.iv);
			plainText = cypher.doFinal(Base64.getDecoder()
					.decode(cypherText));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new String(plainText);
	}

}
