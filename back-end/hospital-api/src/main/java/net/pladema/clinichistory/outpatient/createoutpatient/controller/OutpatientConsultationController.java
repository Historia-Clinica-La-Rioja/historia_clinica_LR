package net.pladema.clinichistory.outpatient.createoutpatient.controller;

import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.CreateOutpatientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class OutpatientConsultationController implements OutpatientConsultationAPI {

    private static final Logger LOG = LoggerFactory.getLogger(OutpatientConsultationController.class);
    public static final String OUTPUT = "Output -> {}";

    @Override
    @Transactional
    public ResponseEntity<Boolean> createOutpatientConsultation(
            Integer institutionId,
            Integer patientId,
            CreateOutpatientDto createOutpatientDto) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, createOutpatientDto {}", institutionId, patientId, createOutpatientDto);

        Boolean result = true;
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }


}