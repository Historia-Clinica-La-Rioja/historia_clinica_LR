package net.pladema.medicalconsultation.diary.controller;

import io.swagger.annotations.Api;
import net.pladema.medicalconsultation.diary.controller.constraints.DiaryOpeningHoursValid;
import net.pladema.medicalconsultation.diary.controller.constraints.DiaryPeriodValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryMapper;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
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


    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
    @Transactional
    public ResponseEntity<DiaryDto> addDiary(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody @Valid @DiaryPeriodValid @DiaryOpeningHoursValid DiaryADto diaryADto) {
        LOG.debug("Input parameters -> diaryADto {}", diaryADto);
        DiaryBo diaryToSave = diaryMapper.toDiaryBo(diaryADto);
        diaryToSave = diaryService.addDiary(diaryToSave);
        DiaryDto result = diaryMapper.toDiaryDto(diaryToSave);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}
