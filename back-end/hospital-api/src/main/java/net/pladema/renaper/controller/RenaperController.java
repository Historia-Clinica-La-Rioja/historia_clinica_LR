package net.pladema.renaper.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.renaper.RenaperServicePool;
import net.pladema.renaper.controller.dto.MedicalCoverageDto;
import net.pladema.renaper.controller.dto.PersonBasicDataResponseDto;
import net.pladema.renaper.controller.mapper.RenaperMapper;
import net.pladema.renaper.services.RenaperService;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import net.pladema.sgx.healthinsurance.service.HealthInsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;


@Tag(name = "Renaper", description = "Renaper")
@RequestMapping("/renaper")
@Slf4j
@RequiredArgsConstructor
@RestController
public class RenaperController {

	private static final String SEARCH_PERSON = "/searchPerson";
	private static final String SEARCH_HEALTH_INSURANCE = "/search-health-insurance";

	private final RenaperService renaperService;
	private final RenaperMapper renaperMapper;
	private final HealthInsuranceService healthInsuranceService;
	private final RenaperServicePool renaperServicePool;

	@GetMapping(value = SEARCH_PERSON)
	public DeferredResult<ResponseEntity<PersonBasicDataResponseDto>> getBasicPerson(
			@RequestParam(value = "identificationNumber", required = true) String identificationNumber,
			@RequestParam(value = "genderId", required = true) Short genderId
	) {
		log.debug("Input data -> identificationNumber: {} , genderId: {} ", identificationNumber, genderId);
		return renaperServicePool.run(SEARCH_PERSON, () -> {
			Optional<PersonBasicDataResponseDto> personData = renaperService.getPersonData(identificationNumber, genderId)
					.map(renaperMapper::fromPersonDataResponse);

            return personData
					.map(personBasicDataResponseDto -> ResponseEntity.ok().body(personBasicDataResponseDto))
					.orElseGet(() -> ResponseEntity.noContent().build());
        });
	}

	@GetMapping(value = SEARCH_HEALTH_INSURANCE)
	public DeferredResult<ResponseEntity<Collection<MedicalCoverageDto>>> getHealthInsurance(
			@RequestParam(value = "identificationNumber") String identificationNumber,
			@RequestParam(value = "genderId") Short genderId
	) {
		log.debug("Input data -> identificationNumber: {} , genderId: {} ", identificationNumber, genderId);
		return renaperServicePool.run(SEARCH_HEALTH_INSURANCE, () -> {
			List<PersonMedicalCoverageBo> medicalCoverageData = renaperService.getPersonMedicalCoverage(identificationNumber, genderId);
			if (!medicalCoverageData.isEmpty()) {
				healthInsuranceService.addAll(medicalCoverageData);
			}
			return ResponseEntity.ok().body(
					renaperMapper.fromPersonMedicalCoverageResponseList(medicalCoverageData)
			);
		});
	}

}
