package net.pladema.clinichistory.hospitalization.controller.maindiagnoses;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.hospitalization.controller.maindiagnoses.dto.MainDiagnosisDto;
import net.pladema.clinichistory.hospitalization.controller.maindiagnoses.mapper.MainDiagnosesMapper;
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
@Tag(name = "Main diagnoses", description = "Main diagnoses")
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
@Validated
public class MainDiagnosesController {

    private static final Logger LOG = LoggerFactory.getLogger(MainDiagnosesController.class);

    public static final String OUTPUT = "Output -> {}";
    
    private final InternmentEpisodeService internmentEpisodeService;

    private final ChangeMainDiagnosesService changeMainDiagnosesService;

    private final MainDiagnosesMapper mainDiagnosesMapper;

    private final PatientExternalService patientExternalService;

    public MainDiagnosesController(InternmentEpisodeService internmentEpisodeService,
                                   ChangeMainDiagnosesService changeMainDiagnosesService,
                                   MainDiagnosesMapper mainDiagnosesMapper,
                                   PatientExternalService patientExternalService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.changeMainDiagnosesService = changeMainDiagnosesService;
        this.mainDiagnosesMapper = mainDiagnosesMapper;
        this.patientExternalService = patientExternalService;
    }

    @PostMapping
    public ResponseEntity<Boolean> addMainDiagnosis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid MainDiagnosisDto mainDiagnosis) {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, mainDiagnosis {}",
                institutionId, internmentEpisodeId, mainDiagnosis);
        MainDiagnosisBo mainDiagnoseBo = mainDiagnosesMapper.fromMainDiagnoseDto(mainDiagnosis);
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