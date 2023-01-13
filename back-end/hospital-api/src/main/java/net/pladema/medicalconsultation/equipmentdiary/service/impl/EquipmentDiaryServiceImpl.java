package net.pladema.medicalconsultation.equipmentdiary.service.impl;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.UpdateAppointmentOpeningHoursService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.appointment.service.domain.EmptyAppointmentBo;
import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.diary.service.DiaryAssociatedProfessionalService;
import net.pladema.medicalconsultation.diary.service.DiaryCareLineService;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.*;
import net.pladema.medicalconsultation.diary.service.exception.DiaryEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundException;
import net.pladema.medicalconsultation.equipmentdiary.repository.EquipmentDiaryRepository;
import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiary;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryOpeningHoursService;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmetDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.lamansys.sgx.shared.dates.utils.DateUtils.getWeekDay;
import static ar.lamansys.sgx.shared.dates.utils.DateUtils.isBetween;
import static ar.lamansys.sgx.shared.security.UserInfo.getCurrentAuditor;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class EquipmentDiaryServiceImpl implements EquipmetDiaryService {

	private static final String INPUT_DIARY_ID = "Input parameters -> diaryId {}";
	private static final Logger LOG = LoggerFactory.getLogger(EquipmentDiaryServiceImpl.class);
	private static final String OUTPUT = "Output -> {}";

	private final EquipmentDiaryOpeningHoursService diaryOpeningHoursService;

	private final AppointmentService appointmentService;

	private final UpdateAppointmentOpeningHoursService updateApmtOHService;

	private final DiaryAssociatedProfessionalService diaryAssociatedProfessionalService;

	private final DiaryCareLineService diaryCareLineService;

	private final EquipmentDiaryRepository equipmentDiaryRepository;

	private final LoggedUserExternalService loggedUserExternalService;

	private final InstitutionExternalService institutionExternalService;

	private final DateTimeProvider dateTimeProvider;

	@Override
	public Integer addDiary(EquipmentDiaryBo equipmentDiaryToSave) throws DiaryException {
		LOG.debug("Input parameters -> equipmentDiaryToSave {}", equipmentDiaryToSave);



		EquipmentDiary equipmentDiary = createDiaryInstance(equipmentDiaryToSave);
		Integer diaryId = persistDiary(equipmentDiaryToSave, equipmentDiary);

		equipmentDiaryToSave.setId(diaryId);
		LOG.debug("EquipmentDiary saved -> {}", equipmentDiaryToSave);
		return diaryId;
	}

	@Transactional
	private Integer persistDiary(EquipmentDiaryBo equipmentDiaryToSave, EquipmentDiary equipmentDiary) {
		equipmentDiary = equipmentDiaryRepository.save(equipmentDiary);
		Integer diaryId = equipmentDiary.getId();
		diaryOpeningHoursService.update(diaryId, equipmentDiaryToSave.getEquipmentDiaryOpeningHours());
		return diaryId;
	}

	private EquipmentDiary createDiaryInstance(EquipmentDiaryBo equipmentDiaryBo) {
		EquipmentDiary equipmentDiary = new EquipmentDiary();
		return mapDiaryBo(equipmentDiaryBo, equipmentDiary);
	}

	private EquipmentDiary mapDiaryBo(EquipmentDiaryBo equipmentDiaryBo, EquipmentDiary diary) {
		diary.setId(equipmentDiaryBo.getId() != null ? equipmentDiaryBo.getId() : null);
		diary.setEquipmentId(equipmentDiaryBo.getEquipmentId());
		diary.setStartDate(equipmentDiaryBo.getStartDate());
		diary.setEndDate(equipmentDiaryBo.getEndDate());
		diary.setAppointmentDuration(equipmentDiaryBo.getAppointmentDuration());
		diary.setAutomaticRenewal(equipmentDiaryBo.isAutomaticRenewal());
		diary.setIncludeHoliday(equipmentDiaryBo.isIncludeHoliday());
		diary.setActive(true);
		return diary;
	}
	@Override
	public List<Integer> getAllOverlappingEquipmentDiaryByEquipment(Integer equipmentId, LocalDate newDiaryStart, LocalDate newDiaryEnd,
																	Short appointmentDuration, Optional<Integer> excludeDiaryId) {
		LOG.debug(
				"Input parameters -> equipmentId {}, newDiaryStart {}, newDiaryEnd {}",
				 equipmentId, newDiaryStart, newDiaryEnd);
		List<Integer> diaryIds = excludeDiaryId.isPresent()
				?equipmentDiaryRepository.findAllOverlappingDiaryByEquipmentExcludingDiary(equipmentId,
				newDiaryStart, newDiaryEnd, appointmentDuration, excludeDiaryId.get())
				:equipmentDiaryRepository.findAllOverlappingDiaryByEquipment(equipmentId,
				newDiaryStart, newDiaryEnd, appointmentDuration);
		LOG.debug("EquipmentDiary saved -> {}", diaryIds);
		return diaryIds;

	}


}
