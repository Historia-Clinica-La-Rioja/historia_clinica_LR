package net.pladema.sgx.service.encryption;

public interface EncryptionService {

    String encrypt(String data) throws Exception;
    String decrypt(String encryptedData) throws Exception;
}

