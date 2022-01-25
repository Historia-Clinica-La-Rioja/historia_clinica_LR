package net.pladema.medicalconsultation.diary.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OccupationDto;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryMapper;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/diaryOpeningHours")
@Tag(name = "Diary Opening Hours", description = "Diary Opening Hours")
public class DiaryOpeningHoursController {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryOpeningHoursController.class);
    public static final String OUTPUT = "Output -> {}";

    private final DiaryOpeningHoursService diaryOpeningHoursService;

    private final DiaryMapper diaryMapper;

    private final LocalDateMapper localDateMapper;

    public DiaryOpeningHoursController(DiaryOpeningHoursService diaryOpeningHoursService,
                                       DiaryMapper diaryMapper,
                                       LocalDateMapper localDateMapper){
        super();
        this.diaryOpeningHoursService = diaryOpeningHoursService;
        this.diaryMapper = diaryMapper;
        this.localDateMapper = localDateMapper;
    }

    /**
     *
     * @param doctorsOfficeId consultorio en el cual iniciar una nueva agenda
     * @param startDateStr fecha de inicio de nueva agenda
     * @param endDateStr fecha de fin de nueva agenda
     * @param diaryId id de la agenda a ignorar
     * @return lista con los días y rangos de horario en los cuales el consultorio {@code doctorsOfficeId}
     * se encuentra ocupado
     */
    @GetMapping(value = "/doctorsOffice/{doctorsOfficeId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<List<OccupationDto>> getAllWeeklyDoctorsOfficeOccupation(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "doctorsOfficeId") Integer doctorsOfficeId,
            @RequestParam(name = "startDate") String startDateStr,
            @RequestParam(name = "endDate") String endDateStr,
            @RequestParam(name = "diaryId", required = false) String diaryId) throws DiaryOpeningHoursException {
        LOG.debug("Input parameters -> doctorsOfficeId {}, startDateStr {}, endDateStr {}, diaryId {}",
                doctorsOfficeId, startDateStr, endDateStr, diaryId);

        LocalDate startDate = localDateMapper.fromStringToLocalDate(startDateStr);
        LocalDate endDate = localDateMapper.fromStringToLocalDate(endDateStr);
        Integer diaryIdParam = (diaryId != null) ? Integer.parseInt(diaryId) : null;
        List<OccupationBo> occupationBos = diaryOpeningHoursService
                .findAllWeeklyDoctorsOfficeOccupation(doctorsOfficeId, startDate, endDate, diaryIdParam);
        List<OccupationDto> result = diaryMapper.toListOccupationDto(occupationBos);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(params = "diaryIds")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<Collection<DiaryOpeningHoursDto>> getMany(@PathVariable(name = "institutionId") Integer institutionId,
                                                                    @RequestParam List<Integer> diaryIds) {
        LOG.debug("Input parameters -> institutionId {}, diaryIds {}", institutionId, diaryIds);
        Collection<DiaryOpeningHoursBo> resultService = diaryOpeningHoursService.getDiariesOpeningHours(diaryIds);
        Collection<DiaryOpeningHoursDto> result = diaryMapper.toListDiaryOpeningHoursDto(resultService);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}
