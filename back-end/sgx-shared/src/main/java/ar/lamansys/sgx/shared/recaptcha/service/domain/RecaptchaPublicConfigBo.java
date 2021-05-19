package ar.lamansys.sgx.shared.recaptcha.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecaptchaPublicConfigBo {

    /**
     * siteKey se envia desde el FE a los servers de google.
     */
    private String siteKey;

    /**
     * Esta activado captcha?
     */
    private boolean enabled;


}
