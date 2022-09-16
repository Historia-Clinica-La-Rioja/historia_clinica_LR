package net.pladema.renaper.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import lombok.RequiredArgsConstructor;
import net.pladema.renaper.controller.dto.MedicalCoverageDto;
import net.pladema.renaper.controller.dto.PersonBasicDataResponseDto;
import net.pladema.renaper.controller.mapper.RenaperMapper;
import net.pladema.renaper.services.RenaperService;
import net.pladema.renaper.services.domain.PersonDataResponse;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import net.pladema.sgx.healthinsurance.service.HealthInsuranceService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/renaper")
@Tag(name = "Renaper", description = "Renaper")
public class RenaperController {

	private static final String SEARCH_PERSON = "/searchPerson";
	private static final String SEARCH_HEALTH_INSURANCE = "/search-health-insurance";


	private static final Logger LOG = LoggerFactory.getLogger(RenaperController.class);

	private static final String TIMEOUT_MSG = "Timeout en WS Renaper";

	private final RenaperService renaperService;

	private final RenaperMapper renaperMapper;
	
	private final HealthInsuranceService healthInsuranceService;

	@Value("${ws.renaper.request.timeout}")
	private long requestTimeOut;


	@GetMapping(value = SEARCH_PERSON)
	public DeferredResult<ResponseEntity<PersonBasicDataResponseDto>> getBasicPerson(
			@RequestParam(value = "identificationNumber", required = true) String identificationNumber,
			@RequestParam(value = "genderId", required = true) Short genderId) {
		LOG.debug("Input data -> identificationNumber: {} , genderId: {} ", identificationNumber, genderId);
		DeferredResult<ResponseEntity<PersonBasicDataResponseDto>> deferredResult = new DeferredResult<>(requestTimeOut);
		setCallbacks(deferredResult,SEARCH_PERSON);
		ForkJoinPool.commonPool().submit(() -> {
			Optional<PersonDataResponse> personData = renaperService.getPersonData(identificationNumber, genderId);
			if (!personData.isPresent()) {
				deferredResult.setResult(ResponseEntity.noContent().build());
			}
			PersonBasicDataResponseDto result = renaperMapper.fromPersonDataResponse(personData.get());
			LOG.debug("Output -> {}", result);
			deferredResult.setResult(ResponseEntity.ok().body(result));
		});
		return deferredResult;
	}

	private <R> void setCallbacks(DeferredResult<ResponseEntity<R>> deferredResult, String serviceName) {
		deferredResult.onTimeout(() -> {
			LOG.error("TimeOut en la invocación del servicio {}", serviceName);
			deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.SC_REQUEST_TIMEOUT).body(TIMEOUT_MSG));
		});
		deferredResult.onError(e -> {
			LOG.error("Error invocando {} ", serviceName);
			deferredResult
					.setErrorResult(ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.toString()));
		});
	}

	@GetMapping(value = SEARCH_HEALTH_INSURANCE)
	public DeferredResult<ResponseEntity<Collection<MedicalCoverageDto>>> getHealthInsurance(
			@RequestParam(value = "identificationNumber") String identificationNumber,
			@RequestParam(value = "genderId") Short genderId) {
		LOG.debug("Input data -> identificationNumber: {} , genderId: {} ", identificationNumber, genderId);
		DeferredResult<ResponseEntity<Collection<MedicalCoverageDto>>> deferredResult = new DeferredResult<>(requestTimeOut);
		setCallbacks(deferredResult,SEARCH_HEALTH_INSURANCE);
		ForkJoinPool.commonPool().submit(() -> {
			List<PersonMedicalCoverageBo> medicalCoverageData = renaperService.getPersonMedicalCoverage(identificationNumber, genderId);
			if (medicalCoverageData.isEmpty()) {
				deferredResult.setResult(ResponseEntity.noContent().build());
			}
			healthInsuranceService.addAll(medicalCoverageData);
			Collection<MedicalCoverageDto> result = renaperMapper.fromPersonMedicalCoverageResponseList(medicalCoverageData);
			deferredResult.setResult(ResponseEntity.ok().body(result));
		});
		return deferredResult;
	}

}
