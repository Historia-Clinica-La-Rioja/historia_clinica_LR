package net.pladema.sgx.healthinsurance.controller;

import io.swagger.annotations.Api;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import net.pladema.sgx.healthinsurance.service.HealthInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/health-insurance")
@Api(value = "Medical coverage", tags = { "Health insurance" })
public class HealthInsuranceController {

    private static final Logger LOG = LoggerFactory.getLogger(HealthInsuranceController.class);

    private static final String OUTPUT = "Output -> {}";

    private final HealthInsuranceService healthInsuranceService;

    public HealthInsuranceController(HealthInsuranceService healthInsuranceService){
        super();
        this.healthInsuranceService = healthInsuranceService;
    }

    @GetMapping
    public ResponseEntity<Collection<PersonMedicalCoverageBo>> getAll(){
        LOG.debug("{}", "All health insurance");
        Collection<PersonMedicalCoverageBo> result = healthInsuranceService.getAll();
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}
