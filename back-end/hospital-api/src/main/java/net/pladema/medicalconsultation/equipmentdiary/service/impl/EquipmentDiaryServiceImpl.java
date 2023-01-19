package net.pladema.medicalconsultation.equipmentdiary.service.impl;



import lombok.RequiredArgsConstructor;

import net.pladema.medicalconsultation.diary.service.exception.DiaryException;
import net.pladema.medicalconsultation.equipmentdiary.repository.EquipmentDiaryRepository;
import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiary;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryOpeningHoursService;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentDiaryServiceImpl implements EquipmentDiaryService {

	private static final String INPUT_DIARY_ID = "Input parameters -> diaryId {}";
	private static final Logger LOG = LoggerFactory.getLogger(EquipmentDiaryServiceImpl.class);
	private static final String OUTPUT = "Output -> {}";

	private final EquipmentDiaryOpeningHoursService equipmentDiaryOpeningHoursService;

	private final EquipmentDiaryRepository equipmentDiaryRepository;

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
		Integer equipmentDiaryId = equipmentDiary.getId();
		equipmentDiaryOpeningHoursService.update(equipmentDiaryId, equipmentDiaryToSave.getEquipmentDiaryOpeningHours());
		return equipmentDiaryId;
	}

	private EquipmentDiary createDiaryInstance(EquipmentDiaryBo equipmentDiaryBo) {
		EquipmentDiary equipmentDiary = new EquipmentDiary();
		return mapDiaryBo(equipmentDiaryBo, equipmentDiary);
	}

	private EquipmentDiary mapDiaryBo(EquipmentDiaryBo equipmentDiaryBo, EquipmentDiary equipmentDiary) {
		equipmentDiary.setId(equipmentDiaryBo.getId() != null ? equipmentDiaryBo.getId() : null);
		equipmentDiary.setEquipmentId(equipmentDiaryBo.getEquipmentId());
		equipmentDiary.setStartDate(equipmentDiaryBo.getStartDate());
		equipmentDiary.setEndDate(equipmentDiaryBo.getEndDate());
		equipmentDiary.setAppointmentDuration(equipmentDiaryBo.getAppointmentDuration());
		equipmentDiary.setAutomaticRenewal(equipmentDiaryBo.isAutomaticRenewal());
		equipmentDiary.setIncludeHoliday(equipmentDiaryBo.isIncludeHoliday());
		equipmentDiary.setActive(true);
		return equipmentDiary;
	}
	@Override
	public List<Integer> getAllOverlappingEquipmentDiaryByEquipment(Integer equipmentId, LocalDate newDiaryStart, LocalDate newDiaryEnd,
																	Short appointmentDuration, Optional<Integer> excludeDiaryId) {
		LOG.debug(
				"Input parameters -> equipmentId {}, newDiaryStart {}, newDiaryEnd {}, appointmentDuration{}",
				 equipmentId, newDiaryStart, newDiaryEnd,appointmentDuration);
		List<Integer> diaryIds = excludeDiaryId.isPresent()
				?equipmentDiaryRepository.findAllOverlappingDiaryByEquipmentExcludingDiary(equipmentId,
				newDiaryStart, newDiaryEnd, appointmentDuration, excludeDiaryId.get())
				:equipmentDiaryRepository.findAllOverlappingDiaryByEquipment(equipmentId,
				newDiaryStart, newDiaryEnd, appointmentDuration);
		LOG.debug("EquipmentDiary saved -> {}", diaryIds);
		return diaryIds;

	}

	@Override
	public List<EquipmentDiaryBo> getAllOverlappingDiary(@NotNull Integer equipmentId,
												@NotNull LocalDate newDiaryStart, @NotNull  LocalDate newDiaryEnd, Optional<Integer> excludeDiaryId) {
		LOG.debug(
				"Input parameters -> doctorsOfficeId {}, newDiaryStart {}, newDiaryEnd {}",
				equipmentId, newDiaryStart, newDiaryEnd);
		List<EquipmentDiary> diaries = excludeDiaryId.isPresent()
				? equipmentDiaryRepository.findAllOverlappingDiaryExcludingDiary(equipmentId,
				newDiaryStart, newDiaryEnd, excludeDiaryId.get())
				: equipmentDiaryRepository.findAllOverlappingDiary(equipmentId, newDiaryStart,
				newDiaryEnd);
		List<EquipmentDiaryBo> result = diaries.stream().map(this::createEquipmentDiaryBoInstance).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

	private EquipmentDiaryBo createEquipmentDiaryBoInstance(EquipmentDiary equipmentDiary) {
		LOG.debug("Input parameters -> equipmentDiary {}", equipmentDiary);
		EquipmentDiaryBo result = new EquipmentDiaryBo();
		result.setId(equipmentDiary.getId());
		result.setEquipmentId(equipmentDiary.getEquipmentId());
		result.setStartDate(equipmentDiary.getStartDate());
		result.setEndDate(equipmentDiary.getEndDate());
		result.setAppointmentDuration(equipmentDiary.getAppointmentDuration());
		result.setAutomaticRenewal(equipmentDiary.isAutomaticRenewal());
		result.setIncludeHoliday(equipmentDiary.isIncludeHoliday());
		result.setDeleted(equipmentDiary.isDeleted());
		LOG.debug(OUTPUT, result);
		return result;
	}


}
