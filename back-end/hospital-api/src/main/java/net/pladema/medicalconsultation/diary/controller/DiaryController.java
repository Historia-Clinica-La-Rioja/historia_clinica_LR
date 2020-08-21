package net.pladema.medicalconsultation.diary.controller;

import io.swagger.annotations.Api;
import net.pladema.medicalconsultation.diary.controller.constraints.*;
import net.pladema.medicalconsultation.diary.controller.dto.CompleteDiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryListDto;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryMapper;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/diary")
@Api(value = "Diary ", tags = { "Diary" })
@Validated
public class DiaryController {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryController.class);
    public static final String OUTPUT = "Output -> {}";

    private final DiaryMapper diaryMapper;

    private final DiaryService diaryService;

    public DiaryController(DiaryMapper diaryMapper,
                           DiaryService diaryService) {
        super();
        this.diaryMapper = diaryMapper;
        this.diaryService = diaryService;
    }

	@GetMapping("/{diaryId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<CompleteDiaryDto> getDiary(@PathVariable(name = "institutionId") Integer institutionId,
			@ValidDiary @PathVariable(name = "diaryId") Integer diaryId) {
		LOG.debug("Input parameters -> institutionId {}, diaryId {}", institutionId, diaryId);
		Optional<CompleteDiaryBo> diaryBo = diaryService.getDiary(diaryId);
		CompleteDiaryDto result = diaryBo.map(diaryMapper::toCompleteDiaryDto).orElse(new CompleteDiaryDto());
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
    @Transactional
    public ResponseEntity<Integer> addDiary(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody @Valid @NewDiaryPeriodValid @DiaryOpeningHoursValid DiaryADto diaryADto) {
        LOG.debug("Input parameters -> diaryADto {}", diaryADto);
        DiaryBo diaryToSave = diaryMapper.toDiaryBo(diaryADto);
        Integer result = diaryService.addDiary(diaryToSave);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
    
    @PutMapping("/{diaryId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
    @Transactional
    public ResponseEntity<Integer> updateDiary(
            @PathVariable(name = "institutionId") Integer institutionId,
            @ValidDiary @PathVariable(name = "diaryId") Integer diaryId,
            @RequestBody @Valid @ExistingDiaryPeriodValid @EditDiaryOpeningHoursValid @DiaryEmptyAppointmentsValid  DiaryDto diaryADto) {
        LOG.debug("Input parameters -> diaryADto {}", diaryADto);
        DiaryBo diaryToUpdate = diaryMapper.toDiaryBo(diaryADto);
        diaryToUpdate.setId(diaryId);
        Integer result = diaryService.updateDiary(diaryToUpdate);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
    
    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ADMINISTRADOR_AGENDA')")
    @ValidDiaryProfessionalId
    public ResponseEntity<Collection<DiaryListDto>> getDiaries(@PathVariable(name = "institutionId")  Integer institutionId,
                                                               @RequestParam(name = "healthcareProfessionalId") Integer healthcareProfessionalId){
        LOG.debug("Input parameters -> institutionId {}, healthcareProfessionalId {}", institutionId, healthcareProfessionalId);
        Collection<DiaryBo> diaryBos = diaryService.getActiveDiariesFromProfessional(healthcareProfessionalId);
        Collection<DiaryListDto> result = diaryMapper.toCollectionDiaryListDto(diaryBos);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{diaryId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "institutionId") Integer institutionId,
                                                     @PathVariable(name = "diaryId") @DiaryDeleteableAppoinmentsValid Integer diaryId) {
        LOG.debug("Input parameters -> institutionId {}, diaryId {}", institutionId, diaryId);
        diaryService.deleteDiary(diaryId);
        LOG.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok(Boolean.TRUE);
    }
}
