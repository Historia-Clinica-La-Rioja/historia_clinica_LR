package net.pladema.sgx.healthinsurance.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.patient.controller.dto.MedicalCoveragePlanDto;
import net.pladema.patient.service.domain.MedicalCoveragePlanBo;
import net.pladema.renaper.controller.dto.MedicalCoverageDto;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import net.pladema.sgx.healthinsurance.controller.mapper.HealthInsuranceMapper;
import net.pladema.sgx.healthinsurance.service.HealthInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/health-insurance")
@Tag(name = "Health insurance", description = "Health insurance")
public class HealthInsuranceController {

    private static final Logger LOG = LoggerFactory.getLogger(HealthInsuranceController.class);

    private static final String OUTPUT = "Output -> {}";

    private final HealthInsuranceService healthInsuranceService;

    private final HealthInsuranceMapper healthInsuranceMapper;

    public HealthInsuranceController(HealthInsuranceService healthInsuranceService,
                                     HealthInsuranceMapper healthInsuranceMapper){
        super();
        this.healthInsuranceService = healthInsuranceService;
        this.healthInsuranceMapper = healthInsuranceMapper;
    }

    @GetMapping
    public ResponseEntity<Collection<MedicalCoverageDto>> getAll(){
        LOG.debug("{}", "All health insurance");
        Collection<PersonMedicalCoverageBo> data = healthInsuranceService.getAll();
        Collection<MedicalCoverageDto> result = healthInsuranceMapper.toMedicalCoverageDtoList(data);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{rnos}")
    public ResponseEntity<MedicalCoverageDto> get(@PathVariable("rnos") Integer rnos){
        LOG.debug("{}", "Get one healthInsurance");
        PersonMedicalCoverageBo data = healthInsuranceService.get(rnos);
        MedicalCoverageDto result = healthInsuranceMapper.toMedicalCoverageDto(data);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

	@GetMapping("/{healthInsuranceId}/plans")
	public ResponseEntity<Collection<MedicalCoveragePlanDto>> getAllPlansById(
			@PathVariable("healthInsuranceId") Integer healthInsuranceId){
		LOG.debug("Input parameters -> healthInsuranceId {}", healthInsuranceId);
		Collection<MedicalCoveragePlanBo> data = healthInsuranceService.getAllPlansByMedicalCoverageId(healthInsuranceId);
		Collection<MedicalCoveragePlanDto> result = healthInsuranceMapper.toMedicalCoveragePlanDtoList(data);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{healthInsuranceId}/health-insurance-plan/{healthInsurancePlanId}")
	public ResponseEntity<MedicalCoveragePlanDto> getPlanById(
			@PathVariable("healthInsuranceId") Integer healthInsuranceId,
			@PathVariable("healthInsurancePlanId") Integer healthInsurancePlanId){
		LOG.debug("Input parameters -> healthInsuranceId {}, healthInsurancePlanId {}",healthInsuranceId, healthInsurancePlanId);
		MedicalCoveragePlanBo data = healthInsuranceService.getPlanById(healthInsurancePlanId);
		MedicalCoveragePlanDto result = healthInsuranceMapper.toMedicalCoveragePlanDto(data);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

}
