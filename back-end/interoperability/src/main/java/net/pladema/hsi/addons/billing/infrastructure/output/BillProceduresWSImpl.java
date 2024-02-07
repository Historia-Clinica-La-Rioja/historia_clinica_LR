package net.pladema.hsi.addons.billing.infrastructure.output;

import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.pladema.hsi.addons.billing.application.BillProceduresException;

import net.pladema.hsi.addons.billing.infrastructure.output.domain.response.BillProceduresResponseDto;
import net.pladema.hsi.addons.billing.infrastructure.output.domain.response.BillProceduresErrorResponseDto;

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

@Slf4j
@ConditionalOnProperty(
		value="ws.addons.billing.enabled",
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

	/**
	 * The addons server offers 2 endpoints for fetching billing data.
	 *  * By encounterId
	 *  * Through a list of snomeds (sctid, pt) pairs.
	 * The workflow is as follows:
	 * 1. Try to fetch the billing data by encounterId. The addons service may not yet have the data
	 * for that encounterId.
	 * 2. If 1 doesn't return anything (or returns 4xx), we call the other endpoint (fetch by snomed)
	 * @param request
	 * @return
	 * @throws BillProceduresException
	 */
	public BillProceduresResponseBo getBilling(BillProceduresRequestBo request) throws BillProceduresException {
		if (request.getEncounterId().isPresent()) {
			return fetchByEncounter(request);
		} else {
			return fetchBySnomeds(request);
		}
	}

	public BillProceduresResponseBo fetchByEncounter(BillProceduresRequestBo request) throws BillProceduresException {
		try {
			Integer encounterId = request.getEncounterId().orElseThrow(() -> new RuntimeException("No hay encounterId"));
			BillProceduresResponseDto response = restClient.exchangeGet(
					billingWSConfig.getEncounterUrl() + "/" + encounterId,
					BillProceduresResponseDto.class
			).getBody().validate();
			return response.toBo(request, true);
		} catch (Exception e) {
			//Just log the exception and try to fetch by snomeds
			log.warn("Fetch por encounterId falló, se continua por lista de snomeds");
			return fetchBySnomeds(request);
		}
	}

	private BillProceduresResponseBo fetchBySnomeds(BillProceduresRequestBo request) throws BillProceduresException {
		try {
			BillProceduresResponseDto response = restClient.exchangePut(
					billingWSConfig.getEncounterUrl(),
					buildRequest(request),
					BillProceduresResponseDto.class
			).getBody().validate();
			return response.toBo(request, false);
		} catch (RestTemplateApiException e) {
			throw processException(e);
		} catch (Exception e) {
			throw processException(e);
		}
	}

	private BillProceduresException processException(Exception e) {
		log.error("Error en el llamado a addons billing: ", e);
		return BillProceduresException.addonsBillingApiException(e.getMessage());
	}

	private BillProceduresException processException(RestTemplateApiException ex) {
		log.error("Error en el llamado a addons billing: ", ex);
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