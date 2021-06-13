package ar.lamansys.sgx.shared.recaptcha.service;

import ar.lamansys.sgx.shared.recaptcha.service.domain.RecaptchaPublicConfigBo;

public interface ICaptchaService {

    void validRecaptcha(String response, String frontUrl);

    boolean isRecaptchaEnable();

    RecaptchaPublicConfigBo getPublicConfig();
}
