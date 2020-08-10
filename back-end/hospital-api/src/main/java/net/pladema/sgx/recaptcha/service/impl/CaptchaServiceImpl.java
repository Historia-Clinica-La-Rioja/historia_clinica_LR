package net.pladema.sgx.recaptcha.service.impl;

import net.pladema.sgx.recaptcha.service.ICaptchaService;
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

    @Value("${RECAPTCHA_SECRET_KEY:6LcVh-UUAAAAAIWVvwCNB3zLdNT7BSAnCZ0cZjwz}")
    private String secretKey;

    @Value("${RECAPTCHA_ENABLE:true}")
    private boolean recaptchaEnable;

    private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    public CaptchaServiceImpl(RestTemplateBuilder restTemplateBuilder){
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public Boolean processResponse(String response, String frontUrl) {
        if(!responseSanityCheck(response))
            return false;

        Map<String, String> body = new HashMap<>();
        body.put("secret", secretKey);
        body.put("response", response);
        body.put("remoteip", getHostFront(frontUrl));
        LOG.debug("Request body for recaptcha: {} " + body);

        ResponseEntity<Map> recaptchaResponseEntity = restTemplateBuilder.build()
                .postForEntity(validatorUrl +
                                "?secret={secret}&response={response}&remoteip={remoteip}",
                                body, Map.class, body);

        LOG.debug("Response from recaptcha: {} " + recaptchaResponseEntity);
        Map<String, Object> responseBody = recaptchaResponseEntity.getBody();

        boolean recaptchaSucess = (Boolean)responseBody.get("success");
        if (!recaptchaSucess) {
            LOG.debug("Captcha request failed", responseBody);
            return false;
        }
        return true;

    }

    public boolean isRecaptchaEnable() {
        return recaptchaEnable;
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
