package net.pladema.clinichistory.hospitalization.controller.maindiagnoses;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.HealthConditionMapper;
import net.pladema.clinichistory.hospitalization.controller.maindiagnoses.dto.MainDiagnosisDto;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.ChangeMainDiagnosesService;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.patient.controller.service.PatientExternalService;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/main-diagnoses")
@Api(value = "Main diagnoses", tags = { "Main diagnoses" })
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
@Validated
public class MainDiagnosesController {

    private static final Logger LOG = LoggerFactory.getLogger(MainDiagnosesController.class);

    public static final String OUTPUT = "Output -> {}";
    
    private final InternmentEpisodeService internmentEpisodeService;

    private final ChangeMainDiagnosesService changeMainDiagnosesService;

    private final HealthConditionMapper healthConditionMapper;

    private final PatientExternalService patientExternalService;

    public MainDiagnosesController(InternmentEpisodeService internmentEpisodeService,
                                   ChangeMainDiagnosesService changeMainDiagnosesService,
                                   HealthConditionMapper healthConditionMapper,
                                   PatientExternalService patientExternalService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.changeMainDiagnosesService = changeMainDiagnosesService;
        this.healthConditionMapper = healthConditionMapper;
        this.patientExternalService = patientExternalService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Boolean> addMainDiagnosis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid MainDiagnosisDto mainDiagnosis) {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, mainDiagnosis {}",
                institutionId, internmentEpisodeId, mainDiagnosis);
        MainDiagnosisBo mainDiagnoseBo = healthConditionMapper.fromMainDiagnoseDto(mainDiagnosis);
        internmentEpisodeService.getPatient(internmentEpisodeId)
                .map(patientExternalService::getBasicDataFromPatient)
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .ifPresentOrElse(mainDiagnoseBo::setPatientInfo,() -> new NotFoundException("El paciente no existe", "El paciente no existe"));
        mainDiagnoseBo.setEncounterId(internmentEpisodeId);
        mainDiagnoseBo.setInstitutionId(institutionId);

        changeMainDiagnosesService.execute(mainDiagnoseBo);

        LOG.debug(OUTPUT, Boolean.TRUE);
        return  ResponseEntity.ok().body(Boolean.TRUE);
    }


}