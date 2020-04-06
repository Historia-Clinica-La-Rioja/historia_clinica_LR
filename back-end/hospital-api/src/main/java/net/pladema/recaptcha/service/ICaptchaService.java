package net.pladema.recaptcha.service;

public interface ICaptchaService {

    public Boolean processResponse(String response, String frontUrl);
    public boolean isRecaptchaEnable();
}
