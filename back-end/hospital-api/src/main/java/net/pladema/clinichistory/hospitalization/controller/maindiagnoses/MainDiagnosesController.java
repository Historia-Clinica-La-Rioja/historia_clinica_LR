package net.pladema.clinichistory.hospitalization.controller.maindiagnoses;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.setpatientfrominternmenteposiode.SetPatientFromInternmentEpisode;
import net.pladema.clinichistory.hospitalization.controller.maindiagnoses.dto.MainDiagnosisDto;
import net.pladema.clinichistory.hospitalization.controller.maindiagnoses.mapper.MainDiagnosesMapper;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.ChangeMainDiagnosesService;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Main diagnoses", description = "Main diagnoses")
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/main-diagnoses")
@Slf4j
@Validated
@RequiredArgsConstructor
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
@RestController
public class MainDiagnosesController {

    public static final String OUTPUT = "Output -> {}";

    private final ChangeMainDiagnosesService changeMainDiagnosesService;

    private final MainDiagnosesMapper mainDiagnosesMapper;

    private final SetPatientFromInternmentEpisode setPatientFromInternmentEpisode;

    @PostMapping
    public ResponseEntity<Boolean> addMainDiagnosis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid MainDiagnosisDto mainDiagnosis) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, mainDiagnosis {}",
                institutionId, internmentEpisodeId, mainDiagnosis);
        MainDiagnosisBo mainDiagnoseBo = mainDiagnosesMapper.fromMainDiagnoseDto(mainDiagnosis);
        mainDiagnoseBo.setEncounterId(internmentEpisodeId);
        mainDiagnoseBo.setInstitutionId(institutionId);
        setPatientFromInternmentEpisode.run(mainDiagnoseBo);
        changeMainDiagnosesService.execute(mainDiagnoseBo);
        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }
}