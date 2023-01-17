package net.pladema.medicalconsultation.equipmentdiary.service.impl;

import ar.lamansys.sgx.shared.dates.repository.entity.EDayOfWeek;
import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.repository.AppointmentAssnRepository;
import net.pladema.medicalconsultation.diary.repository.OpeningHoursRepository;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo;
import net.pladema.medicalconsultation.diary.repository.domain.OccupationVo;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;

import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import net.pladema.medicalconsultation.diary.service.domain.TimeRangeBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;
import net.pladema.medicalconsultation.equipmentdiary.repository.EquipmentDiaryOpeningHoursRepository;
import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiaryOpeningHours;
import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiaryOpeningHoursPK;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryBoMapper;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryOpeningHoursService;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryOpeningHoursBo;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.OpeningHoursBo;

import net.pladema.medicalconsultation.equipmentdiary.service.exception.EEquipmentDiaryOpeningHoursEnumException;
import net.pladema.medicalconsultation.equipmentdiary.service.exception.EquipmentDiaryOpeningHoursException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Service
@RequiredArgsConstructor
public class EquipmentDiaryOpeningHoursServiceImpl implements EquipmentDiaryOpeningHoursService {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentDiaryOpeningHoursServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final EquipmentDiaryOpeningHoursRepository diaryOpeningHoursRepository;

    private final OpeningHoursRepository openingHoursRepository;

	private final AppointmentAssnRepository appointmentAssnRepository;

    private final EquipmentDiaryBoMapper diaryBoMapper;

    @Override
    public void load(Integer diaryId, List<EquipmentDiaryOpeningHoursBo> diaryOpeningHours, List<EquipmentDiaryOpeningHoursBo>... oldOpeningHours) {
        Sort sort = Sort.by("dayWeekId", "from");
        List<OpeningHours> savedOpeningHours = openingHoursRepository.findAll(sort);

        diaryOpeningHours.forEach(doh -> {
            OpeningHoursBo openingHoursBo = doh.getOpeningHours();
            OpeningHours newOpeningHours = diaryBoMapper.toOpeningHours(openingHoursBo);
            Integer openingHoursId;

            //Si los horarios de atención definidos para la agenda ya existen en la BBDD
            // los registros son reutilizados. En caso contrario, son persistidos.
            Optional<OpeningHours> existingOpeningHours = savedOpeningHours.stream()
                    .filter(oh -> oh.equals(newOpeningHours)).findAny();
            if(existingOpeningHours.isPresent())
                openingHoursId = existingOpeningHours.get().getId();
            else {
				openingHoursId = openingHoursRepository.save(newOpeningHours).getId();
				if (oldOpeningHours.length > 0) {
					Optional<EquipmentDiaryOpeningHoursBo> recoveredOpeningHours = oldOpeningHours[0].stream()
							.filter(openingHours -> Objects.equals(openingHours.getOpeningHours().getDayWeekId(), newOpeningHours.getDayWeekId()) && openingHours.getOpeningHours().getFrom().equals(newOpeningHours.getFrom()))
							.findFirst();
					recoveredOpeningHours.ifPresent(diaryOpeningHoursBo -> appointmentAssnRepository.updateOldWithNewOpeningHoursId(diaryOpeningHoursBo.getOpeningHours().getId(), openingHoursId));
				}
			}

            openingHoursBo.setId(openingHoursId);
            diaryOpeningHoursRepository.saveAndFlush(createDiaryOpeningHoursInstance(diaryId, openingHoursId, doh));

        });
    }

	@Override
	public void update(Integer equipmentDiaryId, List<EquipmentDiaryOpeningHoursBo> diaryOpeningHours) {
		List<EquipmentDiaryOpeningHoursBo> oldOpeningHours = diaryOpeningHoursRepository.getDiaryOpeningHours(equipmentDiaryId).stream().map(this::createDiaryOpeningHoursBo).collect(Collectors.toList());
		diaryOpeningHoursRepository.deleteAll(equipmentDiaryId);
		load(equipmentDiaryId, diaryOpeningHours, oldOpeningHours);
	}
    
    private EquipmentDiaryOpeningHours createDiaryOpeningHoursInstance(Integer diaryId, Integer openingHoursId, EquipmentDiaryOpeningHoursBo doh){
		EquipmentDiaryOpeningHours diaryOpeningHours = new EquipmentDiaryOpeningHours();
        diaryOpeningHours.setPk(new EquipmentDiaryOpeningHoursPK(diaryId, openingHoursId));
        diaryOpeningHours.setMedicalAttentionTypeId(doh.getMedicalAttentionTypeId());
        diaryOpeningHours.setOverturnCount((doh.getOverturnCount() != null) ? doh.getOverturnCount() : 0);
        diaryOpeningHours.setExternalAppointmentsAllowed(doh.getExternalAppointmentsAllowed());
        return diaryOpeningHours;
    }

	private EquipmentDiaryOpeningHoursBo createDiaryOpeningHoursBo(DiaryOpeningHoursVo diaryOpeningHoursVo) {
		LOG.debug("Input parameters -> diaryOpeningHoursVo {} ", diaryOpeningHoursVo);
		EquipmentDiaryOpeningHoursBo result = new EquipmentDiaryOpeningHoursBo();
		result.setDiaryId(diaryOpeningHoursVo.getDiaryId());
		result.setMedicalAttentionTypeId(diaryOpeningHoursVo.getMedicalAttentionTypeId());
		result.setOverturnCount(diaryOpeningHoursVo.getOverturnCount());
		result.setOpeningHours(new net.pladema.medicalconsultation.equipmentdiary.service.domain.OpeningHoursBo(diaryOpeningHoursVo.getOpeningHours()));
		result.setExternalAppointmentsAllowed(diaryOpeningHoursVo.getExternalAppointmentsAllowed());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<OccupationBo> findAllWeeklyEquipmentOccupation(Integer equipmentId,
																   LocalDate newDiaryStart,
																   LocalDate newDiaryEnd,
																   Integer ignoreDiaryId) throws EquipmentDiaryOpeningHoursException {
		LOG.debug("Input parameters -> equipmentId {}, startDate {}, endDate {}",
				equipmentId, newDiaryStart, newDiaryEnd);

		validations(equipmentId, newDiaryStart, newDiaryEnd);

		List<OccupationVo> queryResults = diaryOpeningHoursRepository
				.findAllWeeklyEquipmentOccupation(equipmentId, newDiaryStart, newDiaryEnd);

		//Se consideran únicamente los horarios de agendas alcanzados (según calendario)
		// para el rango de fecha newDiarystart y newDiaryEnd
		List<OpeningHours> validQueryResults = new ArrayList<>();
		queryResults.stream()
				.filter(defineFilter(ignoreDiaryId))
				.collect(Collectors.groupingBy(OccupationVo::getDiaryId))
				.forEach( (diaryId,occupationTimeOfDiary)->
						validQueryResults.addAll(
								getOnlyDiaryOverlappingDays(occupationTimeOfDiary, newDiaryStart, newDiaryEnd))
				);

		List<OccupationBo> result = new ArrayList<>();
		validQueryResults.stream()
				.collect(Collectors.groupingBy(OpeningHours::getDayWeekId))
				.forEach( (dayWeekId,openingHours)->
						result.add(mergeRangeTimeOfOpeningHours(dayWeekId, openingHours))
				);
		return result;
	}

	private Predicate<OccupationVo> defineFilter(Integer ignoreDiaryId) {
		LOG.debug("Input parameters -> ignoreDiaryId {}", ignoreDiaryId);
		Predicate<OccupationVo> result = (ignoreDiaryId == null) ? e -> true : e -> !e.getDiaryId().equals(ignoreDiaryId);
		LOG.debug(OUTPUT, result);
		return result;
	}

	private List<OpeningHours> getOnlyDiaryOverlappingDays(List<OccupationVo> occupationTimeOfDiary,
														   LocalDate startDate, LocalDate endDate){
		LocalDate diaryStart = occupationTimeOfDiary.get(0).getStartDate();
		LocalDate diaryEnd = occupationTimeOfDiary.get(0).getEndDate();
		List<Short> overlappingDays = overlappingDays(diaryStart, diaryEnd, startDate, endDate);
		return occupationTimeOfDiary.stream()
				.map(OccupationVo::getOpeningHours)
				.filter(oh -> overlappingDays.contains(oh.getDayWeekId()))
				.collect(Collectors.toList());
	}

	@Override
	public List<Short> overlappingDays(@NotNull LocalDate rangeStart1, @NotNull LocalDate rangeEnd1,
									   @NotNull LocalDate rangeStart2, @NotNull LocalDate rangeEnd2){
		List<Short> validDaysWeek = new ArrayList<>();
		LocalDate start = rangeStart1.isBefore(rangeStart2) ? rangeStart2 : rangeStart1;
		LocalDate end = rangeEnd1.isBefore(rangeStart2) ? rangeEnd1 : rangeEnd2;
		int overlappingDays = end.getDayOfYear() - start.getDayOfYear() + 1;
		if(overlappingDays < 7){
			while(!start.isEqual(end)){
				validDaysWeek.add((short)start.getDayOfWeek().getValue());
				start = start.plusDays(1L);
			}
			validDaysWeek.add((short)start.getDayOfWeek().getValue());
		}
		else
			validDaysWeek.addAll(EDayOfWeek.getAllIds());
		return validDaysWeek;
	}

	private OccupationBo mergeRangeTimeOfOpeningHours(Short dayWeekId, @NotEmpty List<OpeningHours> openingHours){
		Comparator<OpeningHours> ascendingOrder = Comparator
				.comparing(OpeningHours::getFrom, LocalTime::compareTo)
				.thenComparing(OpeningHours::getTo, LocalTime::compareTo);
		openingHours = openingHours.stream().sorted(ascendingOrder).collect(toList());

		List<TimeRangeBo> timeRanges = new ArrayList<>();

		TimeRangeBo lastTimeRange = new TimeRangeBo(openingHours.get(0));
		for(int index = 1; index < openingHours.size(); index ++){
			OpeningHours current = openingHours.get(index);
			if(current.getFrom().isAfter(lastTimeRange.getTo())) {
				timeRanges.add(lastTimeRange);
				lastTimeRange = new TimeRangeBo(current);
			}
			else if (current.getTo().isAfter(lastTimeRange.getTo()))
				lastTimeRange.setTo(current.getTo());
		}
		timeRanges.add(lastTimeRange);

		OccupationBo dayOpeningHours = new OccupationBo();
		dayOpeningHours.setDayWeek(EDayOfWeek.map(dayWeekId));
		dayOpeningHours.setTimeRanges(timeRanges);
		return dayOpeningHours;
	}

	private void validations(Integer equipmentId, LocalDate newDiaryStart, LocalDate newDiaryEnd) throws EquipmentDiaryOpeningHoursException {
		if (equipmentId == null)
			throw new EquipmentDiaryOpeningHoursException(EEquipmentDiaryOpeningHoursEnumException.NULL_EQUIPMENT_ID, "El id del equipo es obligatorio");
		if (newDiaryEnd.isBefore(newDiaryStart))
			throw new EquipmentDiaryOpeningHoursException(EEquipmentDiaryOpeningHoursEnumException.DIARY_END_DATE_BEFORE_START_DATE, String.format("La fecha de fin (%s) de agenda no puede ser previa al inicio (%s)", newDiaryEnd, newDiaryStart));
	}






}
