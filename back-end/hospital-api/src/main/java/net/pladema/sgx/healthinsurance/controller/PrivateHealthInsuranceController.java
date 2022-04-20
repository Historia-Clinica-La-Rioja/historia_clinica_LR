package net.pladema.sgx.healthinsurance.controller;

import net.pladema.patient.controller.dto.MedicalCoveragePlanDto;
import net.pladema.patient.controller.dto.PrivateHealthInsuranceDto;
import net.pladema.patient.service.domain.MedicalCoveragePlanBo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;
import net.pladema.sgx.healthinsurance.controller.mapper.PrivateHealthInsuranceMapper;
import net.pladema.sgx.healthinsurance.service.PrivateHealthInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/private-health-insurance")
//@Api(value = "Private health insurance", tags = { "Private health insurance" })
public class PrivateHealthInsuranceController {

    private static final Logger LOG = LoggerFactory.getLogger(PrivateHealthInsuranceController.class);

    private static final String OUTPUT = "Output -> {}";

    private final PrivateHealthInsuranceService privateHealthInsuranceService;

    private final PrivateHealthInsuranceMapper privateHealthInsuranceMapper;

    public PrivateHealthInsuranceController(PrivateHealthInsuranceService privateHealthInsuranceService,
                                            PrivateHealthInsuranceMapper privateHealthInsuranceMapper) {
        super();
        this.privateHealthInsuranceService = privateHealthInsuranceService;
        this.privateHealthInsuranceMapper = privateHealthInsuranceMapper;
    }

    @GetMapping
    public ResponseEntity<Collection<PrivateHealthInsuranceDto>> getAll() {
        LOG.debug("{}", "All health insurance");
        Collection<PrivateHealthInsuranceBo> data = privateHealthInsuranceService.getAll();
        Collection<PrivateHealthInsuranceDto> result = privateHealthInsuranceMapper.toPrivateHealthInsuranceDtoList(data);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{privateHealthInsuranceId}")
    public ResponseEntity<Collection<MedicalCoveragePlanDto>> getAllPlansById(
            @PathVariable("privateHealthInsuranceId") Integer privateHealthInsuranceId){
        LOG.debug("Input parameters -> privateHealthInsuranceId {}", privateHealthInsuranceId);
        Collection<MedicalCoveragePlanBo> data = privateHealthInsuranceService.getAllPlansByMedicalCoverageId(privateHealthInsuranceId);
        Collection<MedicalCoveragePlanDto> result = privateHealthInsuranceMapper.toMedicalCoveragePlanDtoList(data);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{privateHealthInsuranceId}/private-health-insurance-plan/{privateHealthInsurancePlanId}")
    public ResponseEntity<MedicalCoveragePlanDto> getPlanById(
            @PathVariable("privateHealthInsuranceId") Integer privateHealthInsuranceId,
            @PathVariable("privateHealthInsurancePlanId") Integer privateHealthInsurancePlanId){
        LOG.debug("Input parameters -> privateHealthInsuranceId {}, privateHealthInsurancePlanId {}",privateHealthInsuranceId, privateHealthInsurancePlanId);
        MedicalCoveragePlanBo data = privateHealthInsuranceService.getPlanById(privateHealthInsurancePlanId);
        MedicalCoveragePlanDto result = privateHealthInsuranceMapper.toMedicalCoveragePlanDto(data);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

}
