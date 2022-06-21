package net.pladema.clinichistory.hospitalization.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentEpisodeDto;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentEpisodeProcessDto;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentPatientDto;
import net.pladema.clinichistory.hospitalization.controller.mapper.InternmentEpisodeMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentPatientService;
import net.pladema.clinichistory.hospitalization.service.domain.BasicListedPatientBo;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentEpisodeBo;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentEpisodeProcessBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/institutions/{institutionId}/internments/patients")
@Tag(name = "Internment Patient", description = "Internment Patient")
public class InternmentPatientController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentPatientController.class);
    private static final String OUTPUT = "Output -> {}";
    private static final String INPUT_PARAMETERS_INSTITUTION_ID = "Input parameters -> institutionId {}";

    private final InternmentPatientService internmentPatientService;

    private final InternmentEpisodeMapper internmentEpisodeMapper;

    public InternmentPatientController(InternmentPatientService internmentPatientService,
                                       InternmentEpisodeMapper internmentEpisodeMapper) {
        this.internmentPatientService = internmentPatientService;
        this.internmentEpisodeMapper = internmentEpisodeMapper;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRATIVO, ENFERMERO_ADULTO_MAYOR, ENFERMERO')")
    public ResponseEntity<List<InternmentEpisodeDto>> getAllInternmentPatient(
            @PathVariable(name = "institutionId") Integer institutionId){
        LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID, institutionId);
        List<InternmentEpisodeBo> internmentEpisodes = internmentPatientService.getAllInternmentPatient(institutionId);
        List<InternmentEpisodeDto> result = internmentEpisodeMapper.toListInternmentEpisodeDto(internmentEpisodes);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/basicdata")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRATIVO, ENFERMERO_ADULTO_MAYOR, ENFERMERO')")
    public ResponseEntity<List<InternmentPatientDto>> getAllInternmentPatientsBasicData(
            @PathVariable(name = "institutionId") Integer institutionId){
        LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID, institutionId);
        List<BasicListedPatientBo> basicListedPatientBos = internmentPatientService.getInternmentPatients(institutionId);
        List<InternmentPatientDto> result = internmentEpisodeMapper.toListInternmentPatientDto(basicListedPatientBos);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{patientId}/internmentEpisodeIdInProcess")
    public ResponseEntity<InternmentEpisodeProcessDto> internmentEpisodeIdInProcess(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId){
        LOG.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        InternmentEpisodeProcessBo serviceResult = internmentPatientService.internmentEpisodeInProcess(institutionId,patientId);
        InternmentEpisodeProcessDto result = new InternmentEpisodeProcessDto(serviceResult.getId(), serviceResult.isInProgress(), serviceResult.isPatientHospitalized());
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}