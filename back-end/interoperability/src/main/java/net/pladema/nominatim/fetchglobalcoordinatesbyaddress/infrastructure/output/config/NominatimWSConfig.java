package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.output.config;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@ConfigurationProperties(prefix = "ws.nominatim")
@Component
public class NominatimWSConfig extends WSConfig {

	public NominatimWSConfig(@Value("${ws.nominatim.url.base:}")String baseUrl) {
		super(baseUrl, false);
	}

	@Override
	public String getAbsoluteURL(String relURL) {
		Assert.notNull(relURL, "WS relative URL can not be null");
		return baseUrl + relURL;
	}

}
