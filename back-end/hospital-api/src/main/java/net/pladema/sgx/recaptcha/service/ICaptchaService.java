package net.pladema.sgx.recaptcha.service;

import net.pladema.sgx.recaptcha.service.domain.RecaptchaPublicConfigBo;

public interface ICaptchaService {

    void validRecaptcha(String response, String frontUrl);

    boolean isRecaptchaEnable();

    RecaptchaPublicConfigBo getPublicConfig();
}
