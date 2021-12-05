package ar.lamansys.sgx.auth.user.application.getpasswordtokenexpiration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class GetPasswordTokenExpirationImpl implements GetPasswordTokenExpiration {

    @Value("${password.reset.token.expiration}")
    private Duration passwordTokenExpiration;

    public long execute() {
        log.debug("No input parameters");
        long result = passwordTokenExpiration.toHours();
        log.debug("Output -> {}", result);
        return result;
    }
}
