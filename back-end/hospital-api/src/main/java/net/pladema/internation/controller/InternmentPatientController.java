package net.pladema.internation.controller;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.dto.*;
import net.pladema.internation.controller.mocks.MocksInternmentPatient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/patients")
@Api(value = "Internment Patient", tags = { "Internment Patient" })
public class InternmentPatientController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentPatientController.class);

    public InternmentPatientController() {
        super();
    }
    
    @GetMapping
    public ResponseEntity<List<InternmentEpisodeDto>> getAllInternmentPatient(@PathVariable(name = "institutionId") Integer institutionId){
        List<InternmentEpisodeDto> result = MocksInternmentPatient.mockInternmentPatients(institutionId);
        LOG.debug("Internment patients -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

}