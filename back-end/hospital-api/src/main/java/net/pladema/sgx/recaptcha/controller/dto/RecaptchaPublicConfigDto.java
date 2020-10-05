package net.pladema.sgx.recaptcha.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pladema.sgx.recaptcha.service.domain.RecaptchaPublicConfigBo;

@Getter
@AllArgsConstructor
public class RecaptchaPublicConfigDto {

    /**
     * siteKey se envia desde el FE a los servers de google.
     */
    private String siteKey;

    /**
     * Esta activado captcha?
     */
    private boolean enabled;


    public RecaptchaPublicConfigDto(RecaptchaPublicConfigBo publicConfig) {
        this.enabled = publicConfig.isEnabled();
        this.siteKey = publicConfig.getSiteKey();
    }
}
