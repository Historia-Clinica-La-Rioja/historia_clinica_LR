package ar.lamansys.sgx.shared.recaptcha.service.impl;

import ar.lamansys.sgx.shared.recaptcha.service.ICaptchaService;
import ar.lamansys.sgx.shared.recaptcha.service.domain.RecaptchaPublicConfigBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class CaptchaServiceImpl implements ICaptchaService {

    private static final Logger LOG = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${google.recaptcha.validator.url}")
    private String validatorUrl;

    @Value("${google.recaptcha.secret.key}")
    private String secretKey;

    @Value("${google.recaptcha.site.key}")
    private String siteKey;

    @Value("${google.recaptcha.enable}")
    private boolean recaptchaEnable;

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    public CaptchaServiceImpl(RestTemplateBuilder restTemplateBuilder){
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public void validRecaptcha(String frontUrl, String recaptcha) {
        if (isRecaptchaEnable()) {
            Boolean captchaResponse = processResponse(recaptcha, frontUrl);
            if (!captchaResponse) {
                throw new RecaptchaInvalid();
            }
        }
    }

    public Boolean processResponse(String response, String frontUrl) {
        if(!responseSanityCheck(response))
            return false;

        Map<String, String> body = new HashMap<>();
        body.put("secret", secretKey);
        body.put("response", response);
        body.put("remoteip", getHostFront(frontUrl));
        LOG.debug("Request body for recaptcha: {} ", body);

        ResponseEntity<Map> recaptchaResponseEntity = restTemplateBuilder.build()
                .postForEntity(validatorUrl +
                                "?secret={secret}&response={response}&remoteip={remoteip}",
                                body, Map.class, body);

        LOG.debug("Response from recaptcha: {} ", recaptchaResponseEntity);
        Map<String, Object> responseBody = recaptchaResponseEntity.getBody();

        boolean recaptchaSuccess = (Boolean)responseBody.get("success");
        if (!recaptchaSuccess) {
            LOG.debug("Captcha request failed {}", responseBody);
            return false;
        }
        return true;

    }

    @Override
    public boolean isRecaptchaEnable() {
        return recaptchaEnable;
    }

    @Override
    public RecaptchaPublicConfigBo getPublicConfig() {
        return new RecaptchaPublicConfigBo(this.siteKey, isRecaptchaEnable());
    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    private String getHostFront(String urlFront){
        URI uri = URI.create(urlFront);
        URL url;
        String hostFront = "";
        try {
            url = uri.toURL();
            hostFront = url.getHost();
        } catch (MalformedURLException e) {
            LOG.debug("",e);
        }
        return hostFront;
    }

}
