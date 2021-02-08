package net.pladema.sgx.service.encryption;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Service
public class AsymmetricAESService implements EncryptionService{
    private static final String ALGO = "AES";
    private static final String PROVIDER = "SunJCE";
    private static final String TRANSFORMATION = "/CBC/PKCS5Padding";

    @Value("${integration.covid.encryption.key: ultraSecretKey}")
    private String key;

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data
     *            is a string
     * @return the encrypted string
     */
    public String encrypt(String data) throws Exception {
        Cipher c = Cipher.getInstance(ALGO + TRANSFORMATION, PROVIDER);
        c.init(Cipher.ENCRYPT_MODE, generateKey(), new IvParameterSpec(this.key.getBytes(StandardCharsets.UTF_8)));
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData
     *            is a string
     * @return the decrypted string
     */
    public String decrypt(String encryptedData) throws Exception {
        Cipher c = Cipher.getInstance(ALGO + TRANSFORMATION, PROVIDER);
        c.init(Cipher.DECRYPT_MODE, generateKey(), new IvParameterSpec(this.key.getBytes(StandardCharsets.UTF_8)));
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue, StandardCharsets.UTF_8);
    }

    /**
     * Generate a new encryption key.
     */
    private Key generateKey(){
        return new SecretKeySpec(this.key.getBytes(StandardCharsets.UTF_8), ALGO);
    }
}
