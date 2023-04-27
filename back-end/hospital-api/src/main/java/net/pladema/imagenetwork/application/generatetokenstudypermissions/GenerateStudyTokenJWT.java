package net.pladema.imagenetwork.application.generatetokenstudypermissions;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import ar.lamansys.sgx.shared.token.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@Slf4j
public class GenerateStudyTokenJWT {
    @Value("${token.secret}")
    private String secret;
    @Value("${app.imagenetwork.permission.expiration:60m}")
    private Duration tokenExpiration;

    public String run(String studyInstanceUID) {
        log.debug("Generate token -> studyInstanceUID '{}'", studyInstanceUID);
        String subject = SecurityContextUtils.getUserDetails().getUsername();
        Map<String, Object> claims = Map.of(
                "studyInstanceUID", studyInstanceUID
        );
        String token = JWTUtils.generate(claims, subject, secret, tokenExpiration);
        log.trace("Token generated '{}'", token);
        log.debug("Token generated for studyInstanceUID '{}' access", studyInstanceUID);
        return token;
    }
}
