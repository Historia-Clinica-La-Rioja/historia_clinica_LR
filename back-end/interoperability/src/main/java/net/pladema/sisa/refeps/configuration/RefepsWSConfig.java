package net.pladema.sisa.refeps.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@ConditionalOnProperty(
		value="ws.sisa.enabled",
		havingValue = "true"
)
public class RefepsWSConfig extends WSConfig {

	public final String ENABLED = "Habilitado";

	public RefepsWSConfig(@Value("${ws.sisa.refeps.url.base}") String baseUrl,
						  @Value("${ws.sisa.username}") String username,
						  @Value("${ws.sisa.password}") String password) {
		super(baseUrl + "?usuario=" + username + "&clave=" + password, false);
	}

	@Override
	public String getAbsoluteURL(String relURL) {
		Assert.notNull(relURL, "WS relative URL can not be null");
		return generateURL(this.baseUrl, relURL);
	}

	private String generateURL(String prefix, String suffix) {
		return String.format("%s%s", prefix.replaceAll("/$", ""), suffix.replaceAll("^/", ""));
	}

}
