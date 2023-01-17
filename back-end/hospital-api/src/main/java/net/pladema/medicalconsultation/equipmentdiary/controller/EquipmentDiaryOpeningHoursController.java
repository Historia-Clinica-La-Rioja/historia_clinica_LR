package net.pladema.medicalconsultation.equipmentdiary.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OccupationDto;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryMapper;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;

import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryOpeningHoursService;

import net.pladema.medicalconsultation.equipmentdiary.service.exception.EquipmentDiaryOpeningHoursException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/equipmentDiaryOpeningHours")
@Tag(name = "Equipment Diary Opening Hours", description = "Equipment  Diary Opening Hours")
public class EquipmentDiaryOpeningHoursController {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentDiaryOpeningHoursController.class);
    public static final String OUTPUT = "Output -> {}";

    private final EquipmentDiaryOpeningHoursService diaryOpeningHoursService;

    private final DiaryMapper diaryMapper;

    private final LocalDateMapper localDateMapper;

    public EquipmentDiaryOpeningHoursController(EquipmentDiaryOpeningHoursService diaryOpeningHoursService,
                                                DiaryMapper diaryMapper,
                                                LocalDateMapper localDateMapper){
        super();
        this.diaryOpeningHoursService = diaryOpeningHoursService;
        this.diaryMapper = diaryMapper;
        this.localDateMapper = localDateMapper;
    }

    /**
     *
     * @param equipmentId equipo en el cual iniciar una nueva agenda
     * @param startDateStr fecha de inicio de nueva agenda
     * @param endDateStr fecha de fin de nueva agenda
     * @param equipmentDiaryId id de la agenda a ignorar
     * @return lista con los días y rangos de horario en los cuales el equipo {@code doctorsOfficeId}
     * se encuentra ocupado
     */


	@GetMapping(value = "/equipment/{equipmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<OccupationDto>> getAllWeeklyEquipmentOccupation(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "equipmentId") Integer equipmentId,
			@RequestParam(name = "startDate") String startDateStr,
			@RequestParam(name = "endDate") String endDateStr,
			@RequestParam(name = "equipmentDiaryId", required = false) String equipmentDiaryId) throws EquipmentDiaryOpeningHoursException {
		LOG.debug("Input parameters -> equipmentId {}, startDateStr {}, endDateStr {}, equipmentDiaryId {}",
				equipmentId, startDateStr, endDateStr, equipmentDiaryId);

		LocalDate startDate = localDateMapper.fromStringToLocalDate(startDateStr);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(endDateStr);
		Integer equipmentDiaryIdParam = (equipmentDiaryId != null) ? Integer.parseInt(equipmentDiaryId) : null;
		List<OccupationBo> occupationBos = diaryOpeningHoursService
				.findAllWeeklyEquipmentOccupation(equipmentId, startDate, endDate, equipmentDiaryIdParam);
		List<OccupationDto> result = diaryMapper.toListOccupationDto(occupationBos);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

}
