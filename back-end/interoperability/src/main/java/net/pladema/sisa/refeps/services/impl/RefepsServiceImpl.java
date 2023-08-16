package net.pladema.sisa.refeps.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.lamansys.sgh.shared.infrastructure.output.repository.SharedHealthcareProfessionalRepository;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import lombok.extern.slf4j.Slf4j;
import net.pladema.sisa.refeps.configuration.RefepsWSConfig;
import net.pladema.sisa.refeps.controller.dto.LicenseDataDto;
import net.pladema.sisa.refeps.controller.dto.ValidatedLicenseDataDto;
import net.pladema.sisa.refeps.services.RefepsService;
import net.pladema.sisa.refeps.services.domain.ELicenseNumberType;
import net.pladema.sisa.refeps.services.domain.RefepsLicensePayload;
import net.pladema.sisa.refeps.services.domain.RefepsLicenseSearchResponse;
import net.pladema.sisa.refeps.services.domain.ValidatedLicenseNumberBo;
import net.pladema.sisa.refeps.services.exceptions.RefepsApiException;
import net.pladema.sisa.refeps.services.exceptions.RefepsExceptionsEnum;
import net.pladema.sisa.refeps.services.exceptions.RefepsLicenseException;

@Service
@ConditionalOnProperty(
		value="ws.sisa.enabled",
		havingValue = "true"
)
@Slf4j
public class RefepsServiceImpl implements RefepsService {

	private final RefepsWSConfig refepsWSConfig;

	private final RestClientInterface restClientInterface;

	private final SharedHealthcareProfessionalRepository sharedHealthcareProfessionalRepository;

	private final String NOT_FOUND = "REGISTRO_NO_ENCONTRADO";

	private final String MULTIPLE_RESULTS = "MULTIPLE_RESULTADO";

	@Value("${valid.license.states:#{''}}")
	private List<String> validStates;


	public RefepsServiceImpl(RestTemplateBuilder restTemplateBuilder,
							 RefepsWSConfig wsConfig,
							 SharedHealthcareProfessionalRepository sharedHealthcareProfessionalRepository) {
		this.refepsWSConfig = wsConfig;
		this.restClientInterface = new RestClient(restTemplateBuilder.build(), wsConfig);
		this.sharedHealthcareProfessionalRepository = sharedHealthcareProfessionalRepository;
	}

	@Override
	public List<ValidatedLicenseNumberBo> validateLicenseNumber(Integer healthcareProfessionalId, String identificationNumber, List<String> licenses) throws RefepsApiException, RefepsLicenseException {
		log.debug("Parameters: identificationNumber {}, licenses {}", identificationNumber, licenses);
		List<ValidatedLicenseNumberBo> processedLicenses = new ArrayList<>();
		String responseMessage = null;
		String url = "&nrodoc=" + identificationNumber;
		try {
			RefepsLicenseSearchResponse response = restClientInterface.exchangeGet(url, RefepsLicenseSearchResponse.class).getBody();
			if (responseMessage.equals(MULTIPLE_RESULTS)) {
				url = url.concat("&apellido=" + sharedHealthcareProfessionalRepository.getHealthcareProfessionalLastName(healthcareProfessionalId).toUpperCase());
				response = restClientInterface.exchangeGet(url, RefepsLicenseSearchResponse.class).getBody();
			}
			if (response != null) {
				responseMessage = response.getResultMessage();
				if (response.getResponse() != null) {
					processExistingLicensesNumber(licenses, processedLicenses, response);
					return processedLicenses;
				}
			}
			throw generateCustomException(Objects.requireNonNull(responseMessage));
		} catch (RestTemplateApiException e) {
			throw processRestTemplateException(e);
		}
	}

	@Override
	public List<ValidatedLicenseDataDto> validateLicenseNumberAndType(Integer healthcareProfessionalId, String identificationNumber, List<LicenseDataDto> licensesData) throws RefepsApiException, RefepsLicenseException {
		log.debug("Parameters: identificationNumber {}, licenses {}", identificationNumber, licensesData);
		String responseMessage = null;
		String url = "&nrodoc=" + identificationNumber;
		try {
			RefepsLicenseSearchResponse response = restClientInterface.exchangeGet(url, RefepsLicenseSearchResponse.class).getBody();
			if (responseMessage.equals(MULTIPLE_RESULTS)) {
				url = url.concat("&apellido=" + sharedHealthcareProfessionalRepository.getHealthcareProfessionalLastName(healthcareProfessionalId).toUpperCase());
				response = restClientInterface.exchangeGet(url, RefepsLicenseSearchResponse.class).getBody();
			}
			if (response != null) {
				responseMessage = response.getResultMessage();
				if (response.getResponse() != null)
					return processExistingLicensesData(licensesData, response);
			}
			throw generateCustomException(Objects.requireNonNull(responseMessage));
		} catch (RestTemplateApiException e) {
			throw processRestTemplateException(e);
		}
	}

	private void processExistingLicensesNumber(List<String> licenses, List<ValidatedLicenseNumberBo> processedLicenses, RefepsLicenseSearchResponse response) {
		licenses.forEach(license -> {
			Optional<RefepsLicensePayload> relatedLicenseData = response.getResponse().stream().filter(licenseData -> licenseData.getLicenseNumber().equals(license) && licenseData.getStatus().equals(refepsWSConfig.ENABLED)).findFirst();
			if (relatedLicenseData.isPresent()) {
				if (validStates.stream().filter(state -> state.equals(relatedLicenseData.get().getState())).findFirst().isEmpty())
					throw new RefepsLicenseException(RefepsExceptionsEnum.WRONG_STATE, "La/s matricula/s ingresadas pertenecen a otra jurisdiccion de la permitida");
				processedLicenses.add(new ValidatedLicenseNumberBo(license, true));
			}
			else
				processedLicenses.add(new ValidatedLicenseNumberBo(license, false));
		});
	}

	private List<ValidatedLicenseDataDto> processExistingLicensesData(List<LicenseDataDto> licenses, RefepsLicenseSearchResponse response) {
		List<ValidatedLicenseDataDto> processedLicenses = new ArrayList<>();
		licenses.forEach(license -> {
			Optional<RefepsLicensePayload> relatedLicenseData = response.getResponse().stream().filter(licenseData -> licenseData.getLicenseNumber().equals(license.getLicenseNumber()) && licenseData.getStatus().equals(refepsWSConfig.ENABLED)).findFirst();

			ValidatedLicenseDataDto licenseData = new ValidatedLicenseDataDto();
			initValidatedLicenseDataDto(license, licenseData);

			if (relatedLicenseData.isPresent()) {
				if (validStates.stream().filter(state -> state.equals(relatedLicenseData.get().getState())).findFirst().isEmpty())
					throw new RefepsLicenseException(RefepsExceptionsEnum.WRONG_STATE, "La/s matricula/s ingresadas pertenecen a otra jurisdiccion de la permitida");
				licenseData.setValidLicenseNumber(true);
				Boolean isNationalLicense = license.getLicenseType().equals(ELicenseNumberType.NATIONAL.getId().intValue()) && relatedLicenseData.get().getState().equals("CABA");
				Boolean isStateLicense = license.getLicenseType().equals(ELicenseNumberType.PROVINCE.getId().intValue()) && !relatedLicenseData.get().getState().equals("CABA");
				licenseData.setValidLicenseType(isNationalLicense || isStateLicense);
			}
			else {
				licenseData.setValidLicenseNumber(false);
				licenseData.setValidLicenseType(false);
			}
			processedLicenses.add(licenseData);
		});
		return processedLicenses;
	}

	private void initValidatedLicenseDataDto(LicenseDataDto license, ValidatedLicenseDataDto licenseData) {
		licenseData.setLicenseNumber(license.getLicenseNumber());
		licenseData.setLicenseType(license.getLicenseType());
	}

	private RefepsLicenseException generateCustomException(String responseMessage) {
		if (responseMessage.equals(MULTIPLE_RESULTS))
			return new RefepsLicenseException(RefepsExceptionsEnum.CANNOT_IDENTIFY_PROFESSIONAL, "No es posible identificar al profesional poseedor de la/s matricula/s");
		if (responseMessage.equals(NOT_FOUND))
			return new RefepsLicenseException(RefepsExceptionsEnum.PROFESSIONAL_NOT_FOUND, "El profesional en cuestión no se encuentra registrado, por lo que no cuenta con matriculas");
		return new RefepsLicenseException(RefepsExceptionsEnum.GENERIC_ERROR, "Ha ocurrido un error inesperado. Por favor, inténtelo nuevamente en unos minutos");
	}
	
	private RefepsApiException processRestTemplateException(RestTemplateApiException ex) throws RefepsApiException {
		if (ex.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR)
			throw new RefepsApiException(RefepsExceptionsEnum.SERVER_ERROR, ex.getStatusCode(), "El servicio REFEPS tiene un error interno.");
		if (ex.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			if (ex.getStatusCode() == HttpStatus.REQUEST_TIMEOUT)
				throw new RefepsApiException(RefepsExceptionsEnum.API_TIMEOUT, ex.getStatusCode(), "El servicio REFEPS esta tardando en responder.");
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
				throw new RefepsApiException(RefepsExceptionsEnum.NOT_FOUND, ex.getStatusCode(), "El servicio consultado no existe.");
			if ((ex.getStatusCode() == HttpStatus.BAD_REQUEST) || (ex.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY))) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					String message = mapper.readValue(ex.getBody(), String.class);
					throw new RefepsApiException(RefepsExceptionsEnum.NOT_FOUND, ex.getStatusCode(), message);
				} catch (JsonProcessingException ignored) {}
			}
		}
		throw new RefepsApiException(RefepsExceptionsEnum.UNKNOWN_ERROR, ex.getStatusCode(), ex.getBody());
	}

}

