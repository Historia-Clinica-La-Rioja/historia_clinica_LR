package net.pladema.sisa.refeps.services.impl;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import lombok.extern.slf4j.Slf4j;
import net.pladema.sisa.refeps.services.RefepsService;
import net.pladema.sisa.refeps.configuration.RefepsWSConfig;
import net.pladema.sisa.refeps.services.domain.ValidatedLicenseNumberBo;
import net.pladema.sisa.refeps.services.domain.RefepsLicensePayload;
import net.pladema.sisa.refeps.services.domain.RefepsLicenseSearchResponse;
import net.pladema.sisa.refeps.services.domain.RefepsResourceAttributes;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(
		value="ws.sisa.enabled",
		havingValue = "true"
)
@Slf4j
public class RefepsServiceImpl implements RefepsService {

	private final RefepsWSConfig refepsWSConfig;

	private final RestClientInterface restClientInterface;

	public RefepsServiceImpl(RestTemplateBuilder restTemplateBuilder,
							 RefepsWSConfig wsConfig) {
		this.refepsWSConfig = wsConfig;
		this.restClientInterface = new RestClient(restTemplateBuilder.build(), wsConfig);
	}

	@Override
	public List<ValidatedLicenseNumberBo> validateLicenseNumber(RefepsResourceAttributes attributes, List<String> licenses) throws RestTemplateApiException {
		log.debug("Parameters: {}", attributes);
		List<ValidatedLicenseNumberBo> processedLicenses = new ArrayList<>();
		String url = "&apellido=" + attributes.getLastName() + "&dni=" + attributes.getId();
		RefepsLicenseSearchResponse response = restClientInterface.exchangeGet(url, RefepsLicenseSearchResponse.class).getBody();
		if (response.getResponse() != null) {
			licenses.forEach(license -> {
				Optional<RefepsLicensePayload> relatedLicenseData = response.getResponse().stream().filter(licenseData -> licenseData.getLicenseNumber().equals(license)).findFirst();
				if (relatedLicenseData.isPresent()) {
					if (!relatedLicenseData.get().getState().equals(refepsWSConfig.ENABLED))
						processedLicenses.add(new ValidatedLicenseNumberBo(license, false));
					else
						processedLicenses.add(new ValidatedLicenseNumberBo(license, true));
				}
			});
			return processedLicenses;
		}
		return null;
	}

}

