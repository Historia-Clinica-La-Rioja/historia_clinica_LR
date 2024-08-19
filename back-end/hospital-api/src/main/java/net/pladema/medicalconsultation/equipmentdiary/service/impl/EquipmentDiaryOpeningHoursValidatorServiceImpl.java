package net.pladema.medicalconsultation.equipmentdiary.service.impl;

import lombok.RequiredArgsConstructor;

import net.pladema.medicalconsultation.equipmentdiary.repository.EquipmentDiaryOpeningHoursRepository;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryOpeningHoursService;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryOpeningHoursValidatorService;

import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryOpeningHoursBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EquipmentDiaryOpeningHoursValidatorServiceImpl implements EquipmentDiaryOpeningHoursValidatorService {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentDiaryOpeningHoursValidatorServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final EquipmentDiaryOpeningHoursRepository equipmentDiaryOpeningHoursRepository;

    private final EquipmentDiaryService diaryService;

    private final EquipmentDiaryOpeningHoursService equipmentDiaryOpeningHoursService;



    @Override
    public boolean overlapDiaryOpeningHours(@NotNull EquipmentDiaryBo equipmentDiaryBo, @NotNull List<EquipmentDiaryOpeningHoursBo> openingHours) {
        LOG.debug("Input parameters -> diaryBo {}, openingHours {} ", equipmentDiaryBo, openingHours);
        boolean overlap = overlapBetweenDiaryOpeningHours(openingHours) ||
               overlapWithOthersDiaries(equipmentDiaryBo, openingHours);
        LOG.debug(OUTPUT, overlap);
        return overlap;
    }

    private boolean overlapBetweenDiaryOpeningHours(@NotNull List<EquipmentDiaryOpeningHoursBo> openingHours) {
        LOG.debug("Input parameters -> openingHours {}", openingHours);
        boolean overlap = false;

        int index = 0;
		EquipmentDiaryOpeningHoursBo last = openingHours.get(index++);
		EquipmentDiaryOpeningHoursBo current;

        while (index < openingHours.size() && !overlap) {
            current = openingHours.get(index++);
            overlap = last.overlap(current);
            last = current;
        }
        LOG.debug(OUTPUT, overlap);
        return overlap;
    }

    private boolean overlapWithOthersDiaries(@NotNull EquipmentDiaryBo equipmentDiaryBo, @NotNull  List<EquipmentDiaryOpeningHoursBo> openingHours){
        LOG.debug("Input parameters -> diaryBo {}, openingHours {}", equipmentDiaryBo, openingHours);
        List<EquipmentDiaryBo> overlapDiaries = diaryService.getAllOverlappingDiary(equipmentDiaryBo.getEquipmentId(), equipmentDiaryBo.getStartDate(),
				equipmentDiaryBo.getEndDate(), Optional.ofNullable(equipmentDiaryBo.getId()));
        boolean overlap = overlapDiaries.stream().anyMatch(od ->
				equipmentDiaryBo.getDiaryOpeningHours().stream().anyMatch(doh -> overlapWithOthers(equipmentDiaryBo, od, doh)));
        LOG.debug(OUTPUT, overlap);
        return overlap;
    }

    private boolean overlapWithOthers(@NotNull EquipmentDiaryBo equipmentDiaryBo, @NotNull EquipmentDiaryBo overlapDiary, @NotNull EquipmentDiaryOpeningHoursBo equipmentDiaryOpeningHoursBo) {
        LOG.debug("Input parameters -> equipmentDiaryBo {}, overlapDiary {}, equipmentDiaryOpeningHoursBo {}", equipmentDiaryBo, overlapDiary, equipmentDiaryOpeningHoursBo);
        Objects.requireNonNull(equipmentDiaryBo.getEquipmentId());
        Objects.requireNonNull(overlapDiary.getEquipmentId());
        Objects.requireNonNull(equipmentDiaryOpeningHoursBo.getOpeningHours());
        Objects.requireNonNull(equipmentDiaryOpeningHoursBo.getOpeningHours().getDayWeekId());

        if (!equipmentDiaryBo.getEquipmentId().equals(overlapDiary.getEquipmentId())) {
            LOG.debug(OUTPUT, Boolean.FALSE);
            return Boolean.FALSE;
        }

        List<Short> overlappingDays = equipmentDiaryOpeningHoursService.overlappingDays(equipmentDiaryBo.getStartDate(),
				equipmentDiaryBo.getEndDate(), overlapDiary.getStartDate(), overlapDiary.getEndDate());

        if (!overlappingDays.contains(equipmentDiaryOpeningHoursBo.getOpeningHours().getDayWeekId())) {
            LOG.debug(OUTPUT, Boolean.FALSE);
            return Boolean.FALSE;
        }

        boolean result = equipmentDiaryOpeningHoursRepository.overlapDiaryOpeningHoursFromOtherDiary(overlapDiary.getId(),
                overlapDiary.getEquipmentId(),
				equipmentDiaryOpeningHoursBo.getOpeningHours().getDayWeekId(),
				equipmentDiaryOpeningHoursBo.getOpeningHours().getFrom(),
				equipmentDiaryOpeningHoursBo.getOpeningHours().getTo());
        LOG.debug(OUTPUT, result);
        return result;
    }

	@Override
	public boolean allowNewOverturn(Integer equipmentDiaryId, Integer openingHoursId, LocalDate newApmtDate) {
		LOG.debug("Input parameters -> equipmentDiaryId {}, openingHoursId {}", equipmentDiaryId, openingHoursId);
		boolean result = equipmentDiaryOpeningHoursRepository.allowNewOverturn(equipmentDiaryId, openingHoursId, newApmtDate);
		LOG.debug(OUTPUT, result);
		return result;
	}


}
