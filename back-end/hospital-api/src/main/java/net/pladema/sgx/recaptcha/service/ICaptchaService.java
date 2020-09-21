package net.pladema.sgx.recaptcha.service;

public interface ICaptchaService {

    public void validRecaptcha(String response, String frontUrl);
    public boolean isRecaptchaEnable();
}
