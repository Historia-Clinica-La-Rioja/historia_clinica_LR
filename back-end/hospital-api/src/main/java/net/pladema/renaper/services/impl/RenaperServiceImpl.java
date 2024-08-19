package net.pladema.renaper.services.impl;

import ar.lamansys.sgx.shared.restclient.services.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.pladema.renaper.configuration.RenaperCondition;
import net.pladema.renaper.configuration.RenaperRestTemplateAuth;
import net.pladema.renaper.configuration.RenaperWSConfig;
import net.pladema.renaper.services.RenaperService;
import net.pladema.renaper.services.domain.PersonDataResponse;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import net.pladema.renaper.services.domain.RenaperServiceException;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Conditional(RenaperCondition.class)
@Service
public class RenaperServiceImpl extends RestClient implements RenaperService {

	private final RenaperWSConfig renaperWSConfig;

	private final ObjectMapper jackson;

	public RenaperServiceImpl(RenaperRestTemplateAuth restTemplateAuth, RenaperWSConfig wsConfig,
							  ObjectMapper jackson) {
		super(restTemplateAuth, wsConfig);
		this.renaperWSConfig = wsConfig;
		this.jackson = jackson;
	}

	@Override
	public List<PersonMedicalCoverageBo> getPersonMedicalCoverage(String nroDocumento, Short idSexo) throws RenaperServiceException {
		String urlWithParams = renaperWSConfig.getUrlCobertura() + "?nroDocumento=" + nroDocumento + "&idSexo=" + idSexo;
		try{
			ResponseEntity<PersonMedicalCoverageBo[]> response = exchangeGet(urlWithParams,
				PersonMedicalCoverageBo[].class);
			if (wasResult2xx(response)) {
				var body = response.getBody();
				if (body == null) {
					return Collections.emptyList();
				}
				return Arrays.asList(body);
			}
			throw new RenaperServiceException(String.format("Bad status code %s", response.getStatusCode()));
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new RenaperServiceException(e.getMessage());
		}
	}

	@Override
	public Optional<PersonDataResponse> getPersonData(String nroDocumento, Short idSexo) throws RenaperServiceException {
		String urlWithParams = renaperWSConfig.getUrlPersona() + "?nroDocumento=" + nroDocumento + "&idSexo=" + idSexo;
		try{
			ResponseEntity<String> response = exchangeGet(urlWithParams, String.class);
			if (wasResult2xx(response)) {
				String body = response.getBody();
				if (body == null) {
					return Optional.empty();
				}
				return Optional.of(jackson.readValue(body, PersonDataResponse.class));
			}
			throw new RenaperServiceException(String.format("Bad status code %s", response.getStatusCode()));
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new RenaperServiceException(e.getMessage());
		}
	}

	private static boolean wasResult2xx(ResponseEntity<?> response) {
		HttpStatus statusCode = response.getStatusCode();
		return statusCode.is2xxSuccessful();
	}

}
