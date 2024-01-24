package net.pladema.hsi.addons.billing.infrastructure.output;

import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.pladema.hsi.addons.billing.application.BillProceduresException;

import net.pladema.hsi.addons.billing.infrastructure.output.domain.BillProceduresErrorResponseDto;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hsi.addons.billing.application.port.BillProceduresPort;
import net.pladema.hsi.addons.billing.domain.BillProceduresRequestBo;
import net.pladema.hsi.addons.billing.domain.BillProceduresResponseBo;
import net.pladema.hsi.addons.billing.infrastructure.output.domain.BillProceduresRequestDto;
import net.pladema.hsi.addons.billing.infrastructure.output.domain.BillProceduresResponseDto;

@Slf4j
@ConditionalOnProperty(
		value="app.addons.billing.enabled",
		havingValue = "true"
)
@Service
public class BillProceduresWSImpl implements BillProceduresPort {

	private final RestClientInterface restClient;
	private BillProceduresWSConfig billingWSConfig;

	public BillProceduresWSImpl(HttpClientConfiguration httpClientConfiguration, BillProceduresWSConfig billingWSConfig) throws Exception {
		this.billingWSConfig = billingWSConfig;
		this.restClient = new RestClient(new RestTemplateSSL(httpClientConfiguration), new WSConfig(billingWSConfig.getUrl(), false)) {
			@Override
			public HttpHeaders getHeaders() {
				var headers = super.getHeaders();
				if (billingWSConfig.getAppKey() != null) {
					headers.add(billingWSConfig.getAppKeyHeader(), billingWSConfig.getAppKey());
				}
				return headers;
			}
		};
	}

	public BillProceduresResponseBo getBilling(BillProceduresRequestBo request) throws BillProceduresException {
		try {
			BillProceduresResponseDto response = restClient.exchangePut(
				billingWSConfig.getEncounterUrl(),
				buildRequest(request),
				BillProceduresResponseDto.class
				).getBody().validate();
			return response.toBo();
		} catch (RestTemplateApiException e) {
			throw processException(e);
		}
	}

	private BillProceduresException processException(RestTemplateApiException ex) {
		log.error("Error en el llamado a addons billing: {}", ex);
		//Errores 5xx
		if (ex.getStatusCode().series().equals(HttpStatus.Series.SERVER_ERROR)) {

			ObjectMapper mapper = new ObjectMapper();
			try {
				BillProceduresErrorResponseDto error = mapper.readValue(ex.getBody(), BillProceduresErrorResponseDto.class);
				return BillProceduresException.addonsBillingApiException(error.getMessage());
			} catch (JsonProcessingException e) {
				return BillProceduresException.addonsBillingApiExceptionUnparsed(ex.getBody());
			}
		}

		//Errores 4xx
		if (ex.getStatusCode().series().equals(HttpStatus.Series.CLIENT_ERROR)) {
			if (ex.getStatusCode().equals(HttpStatus.REQUEST_TIMEOUT))
				return BillProceduresException.addonsBillingApiTimeoutException(ex.getMessage());
			if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND))
				return BillProceduresException.addonsBillingApiNotFoundException(ex.getMessage());
			if (ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
				return BillProceduresException.addonsBillingApiUnauthorized(ex.getMessage());
		}

		return BillProceduresException.addonsBillingApiUnknown(ex.getMessage());
	}

	private BillProceduresRequestDto buildRequest(BillProceduresRequestBo request) {
		return new BillProceduresRequestDto(
			request.getProcedures().stream()
				.map(p -> new BillProceduresRequestDto.BillProceduresRequestItemDto(p.getSctid(), p.getPt()))
				.collect(Collectors.toList()),
			request.getMedicalCoverageCuit(),
			request.getDate()
		);
	}
}