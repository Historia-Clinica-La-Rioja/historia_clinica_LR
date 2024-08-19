package net.pladema.medicalconsultation.equipmentdiary.controller;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.equipmentdiary.application.UpdateEquipmentDiaryAndAppointments;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.EditEquipmentDiaryOpeningHoursValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.EquipmentDiaryEmptyAppointmentsValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.EquipmentDiaryOpeningHoursValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.ExistingEquipmentDiaryPeriodValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.NewDiaryPeriodValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.ValidEquipmentDiary;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.CompleteEquipmentDiaryDto;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryADto;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryDto;
import net.pladema.medicalconsultation.equipmentdiary.controller.mapper.EquipmentDiaryMapper;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryBoMapper;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.CompleteEquipmentDiaryBo;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "EquipmentDiary", description = "EquipmentDiary")
@RequestMapping("/institutions/{institutionId}/medicalConsultations/equipmentDiary")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class EquipmentDiaryController {

    public static final String OUTPUT = "Output -> {}";

    private final EquipmentDiaryMapper equipmentDiaryMapper;
    private final EquipmentDiaryService equipmentDiaryService;
    private final EquipmentDiaryBoMapper equipmentDiaryBoMapper;
    private final FeatureFlagsService featureFlagsService;
    private final UpdateEquipmentDiaryAndAppointments updateEquipmentDiaryAndAppointments;

    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<Integer> addDiary(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody @Valid @NewDiaryPeriodValid @EquipmentDiaryOpeningHoursValid EquipmentDiaryADto equipmentDiaryADto) {
        if (!featureFlagsService.isOn(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES))
            return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
        log.debug("Input parameters -> institutionId {}, diaryADto {}", institutionId, equipmentDiaryADto);
        EquipmentDiaryBo equipmentdiaryToSave = equipmentDiaryMapper.toEquipmentDiaryBo(equipmentDiaryADto);
        Integer result = equipmentDiaryService.addDiary(equipmentdiaryToSave);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/equipment/{equipmentId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<List<EquipmentDiaryDto>> getEquipmentDiariesFromEquipment(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "equipmentId") Integer equipmentId) {
        List<EquipmentDiaryBo> equipmentDiariesBOActive = equipmentDiaryService.getEquipmentDiariesFromEquipment(equipmentId, true);
        List<EquipmentDiaryDto> result = equipmentDiaryBoMapper.toListEquipmentDiaryDto(equipmentDiariesBOActive);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{equipmentDiaryId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<CompleteEquipmentDiaryDto> getDiary(
            @PathVariable(name = "institutionId") Integer institutionId,
            @ValidEquipmentDiary @PathVariable(name = "equipmentDiaryId") Integer equipmentDiaryId) {
        log.debug("Input parameters -> institutionId {}, equipmentDiaryId {}", institutionId, equipmentDiaryId);
        Optional<CompleteEquipmentDiaryBo> completeEquipmnetDiaryBo = equipmentDiaryService.getEquipmentDiary(equipmentDiaryId);
        CompleteEquipmentDiaryDto result = completeEquipmnetDiaryBo.map(equipmentDiaryMapper::toCompleteEquipmentDiaryDto).orElse(new CompleteEquipmentDiaryDto());
        log.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{equipmentDiaryId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<Integer> updateEquipmentDiaryAndAppointments(
            @PathVariable(name = "institutionId") Integer institutionId,
            @ValidEquipmentDiary @PathVariable(name = "equipmentDiaryId") Integer equipmentDiaryId,
            @RequestBody @Valid @ExistingEquipmentDiaryPeriodValid @EditEquipmentDiaryOpeningHoursValid
            @EquipmentDiaryEmptyAppointmentsValid EquipmentDiaryDto equipmentDiaryDto) {
        log.debug("Input parameters -> institutionId {}, equipmentDiaryId {}, equipmentDiaryDto {}",
                institutionId, equipmentDiaryId, equipmentDiaryDto);
        EquipmentDiaryBo diaryToUpdate = equipmentDiaryMapper.toEquipmentDiaryBo(equipmentDiaryDto);
        diaryToUpdate.setId(equipmentDiaryId);
        Integer result = updateEquipmentDiaryAndAppointments.run(diaryToUpdate);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}
