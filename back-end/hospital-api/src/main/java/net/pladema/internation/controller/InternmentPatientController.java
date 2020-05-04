package net.pladema.internation.controller;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.dto.InternmentEpisodeDto;
import net.pladema.internation.controller.dto.internmentpatient.InternmentPatientDto;
import net.pladema.internation.controller.mapper.InternmentEpisodeMapper;
import net.pladema.internation.controller.mocks.MocksInternmentPatient;
import net.pladema.internation.service.InternmentPatientService;
import net.pladema.internation.service.domain.internment.BasicListedPatientBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/patients")
@Api(value = "Internment Patient", tags = { "Internment Patient" })
public class InternmentPatientController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentPatientController.class);

    private final InternmentPatientService internmentPatientService;

    private final InternmentEpisodeMapper internmentEpisodeMapper;

    public InternmentPatientController(InternmentPatientService internmentPatientService, InternmentEpisodeMapper internmentEpisodeMapper) {
        this.internmentPatientService = internmentPatientService;
        this.internmentEpisodeMapper = internmentEpisodeMapper;
    }

    @GetMapping
    public ResponseEntity<List<InternmentEpisodeDto>> getAllInternmentPatient(@PathVariable(name = "institutionId") Integer institutionId){
        List<InternmentEpisodeDto> result = MocksInternmentPatient.mockInternmentPatients(institutionId);
        LOG.debug("Internment patients -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/basicdata")
    public ResponseEntity<List<InternmentPatientDto>> getAllInternmentPatientsBasicData(
            @PathVariable(name = "institutionId") Integer institutionId){
        LOG.debug("Input parameters -> {}", institutionId);
        List<BasicListedPatientBo> basicListedPatientBos = internmentPatientService.getInternmentPatients(institutionId);
        List<InternmentPatientDto> result = internmentEpisodeMapper.toListInternmentPatientDto(basicListedPatientBos);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

}