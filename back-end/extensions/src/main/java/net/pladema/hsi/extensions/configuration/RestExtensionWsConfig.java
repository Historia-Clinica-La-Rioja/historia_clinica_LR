package net.pladema.hsi.extensions.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hsi.extensions.rest")
@Getter
@Setter
public class RestExtensionWsConfig extends WSConfig {

    @Value("${hsi.extensions.rest.timeout:5000}")
    private Integer timeout;

    public RestExtensionWsConfig(@Value("${hsi.extensions.rest.url:}") String baseUrl) {
        super(baseUrl, false);
    }
}
