package net.pladema.medicalconsultation.diary.service.impl;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.repository.AppointmentAssnRepository;
import net.pladema.medicalconsultation.diary.service.domain.*;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.pladema.medicalconsultation.diary.repository.DiaryOpeningHoursRepository;
import net.pladema.medicalconsultation.diary.repository.OpeningHoursRepository;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo;
import net.pladema.medicalconsultation.diary.repository.domain.OccupationVo;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHours;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHoursPK;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;
import net.pladema.medicalconsultation.diary.service.DiaryBoMapper;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import ar.lamansys.sgx.shared.dates.repository.entity.EDayOfWeek;

@Service
@RequiredArgsConstructor
public class DiaryOpeningHoursServiceImpl implements DiaryOpeningHoursService {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryOpeningHoursServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DiaryOpeningHoursRepository diaryOpeningHoursRepository;

    private final OpeningHoursRepository openingHoursRepository;

	private final AppointmentAssnRepository appointmentAssnRepository;

    private final DiaryBoMapper diaryBoMapper;

    @Override
    public void load(Integer diaryId, List<DiaryOpeningHoursBo> diaryOpeningHours, List<DiaryOpeningHoursBo>... oldOpeningHours) {
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
					Optional<DiaryOpeningHoursBo> recoveredOpeningHours = oldOpeningHours[0].stream()
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
	public void update(Integer diaryId, List<DiaryOpeningHoursBo> diaryOpeningHours) {
		List<DiaryOpeningHoursBo> oldOpeningHours = diaryOpeningHoursRepository.getDiaryOpeningHours(diaryId).stream().map(this::createDiaryOpeningHoursBo).collect(Collectors.toList());
		diaryOpeningHoursRepository.deleteAll(diaryId);
		load(diaryId, diaryOpeningHours, oldOpeningHours);
	}
    
    private DiaryOpeningHours createDiaryOpeningHoursInstance(Integer diaryId, Integer openingHoursId, DiaryOpeningHoursBo doh){
        DiaryOpeningHours diaryOpeningHours = new DiaryOpeningHours();
        diaryOpeningHours.setPk(new DiaryOpeningHoursPK(diaryId, openingHoursId));
        diaryOpeningHours.setMedicalAttentionTypeId(doh.getMedicalAttentionTypeId());
        diaryOpeningHours.setOverturnCount((doh.getOverturnCount() != null) ? doh.getOverturnCount() : 0);
        diaryOpeningHours.setExternalAppointmentsAllowed(doh.getExternalAppointmentsAllowed());
        return diaryOpeningHours;
    }

    @Override
    public List<OccupationBo> findAllWeeklyDoctorsOfficeOccupation(Integer doctorOfficeId,
                                                                   LocalDate newDiaryStart,
                                                                   LocalDate newDiaryEnd,
                                                                   Integer ignoreDiaryId) throws DiaryOpeningHoursException {
        LOG.debug("Input parameters -> doctorOfficeId {}, startDate {}, endDate {}",
                doctorOfficeId, newDiaryStart, newDiaryEnd);
        
        validations(doctorOfficeId, newDiaryStart, newDiaryEnd);

        List<OccupationVo> queryResults = diaryOpeningHoursRepository
                .findAllWeeklyDoctorsOfficeOccupation(doctorOfficeId, newDiaryStart, newDiaryEnd);

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

    private void validations(Integer doctorOfficeId, LocalDate newDiaryStart, LocalDate newDiaryEnd) throws DiaryOpeningHoursException {
        if (doctorOfficeId == null)
            throw new DiaryOpeningHoursException(DiaryOpeningHoursEnumException.NULL_DOCTOR_OFFICE_ID, "El id del consultorio es obligatorio");
        if (newDiaryEnd.isBefore(newDiaryStart))
            throw new DiaryOpeningHoursException(DiaryOpeningHoursEnumException.DIARY_END_DATE_BEFORE_START_DATE, String.format("La fecha de fin (%s) de agenda no puede ser previa al inicio (%s)", newDiaryEnd, newDiaryStart));
    }

    private Predicate<OccupationVo> defineFilter(Integer ignoreDiaryId) {
        LOG.debug("Input parameters -> ignoreDiaryId {}", ignoreDiaryId);
        Predicate<OccupationVo> result = (ignoreDiaryId == null) ? e -> true : e -> !e.getDiaryId().equals(ignoreDiaryId);
        LOG.debug(OUTPUT, result);
        return result;
    }



    @Override
    public Collection<DiaryOpeningHoursBo> getDiariesOpeningHours(List<Integer> diaryIds) {
        LOG.debug("Input parameters -> diaryIds {} ", diaryIds);
        Collection<DiaryOpeningHoursBo> result = new ArrayList<>();
        if (!diaryIds.isEmpty()) {
            List<DiaryOpeningHoursVo> resultQuery = diaryOpeningHoursRepository.getDiariesOpeningHours(diaryIds);
            result = resultQuery.stream().map(this::createDiaryOpeningHoursBo).collect(Collectors.toList());
        }
        LOG.debug(OUTPUT, result);
        return result;
    }

	@Override
	public Collection<DiaryOpeningHoursBo> getDiaryOpeningHours(Integer diaryId) {
		LOG.debug("Input parameters -> diaryId {} ", diaryId);
		Collection<DiaryOpeningHoursBo> result = new ArrayList<>();
		List<DiaryOpeningHoursVo> resultQuery = diaryOpeningHoursRepository.getDiaryOpeningHours(diaryId);
		result = resultQuery.stream().map(this::createDiaryOpeningHoursBo).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

    private DiaryOpeningHoursBo createDiaryOpeningHoursBo(DiaryOpeningHoursVo diaryOpeningHoursVo) {
        LOG.debug("Input parameters -> diaryOpeningHoursVo {} ", diaryOpeningHoursVo);
        DiaryOpeningHoursBo result = new DiaryOpeningHoursBo();
        result.setDiaryId(diaryOpeningHoursVo.getDiaryId());
        result.setMedicalAttentionTypeId(diaryOpeningHoursVo.getMedicalAttentionTypeId());
        result.setOverturnCount(diaryOpeningHoursVo.getOverturnCount());
        result.setOpeningHours(new OpeningHoursBo(diaryOpeningHoursVo.getOpeningHours()));
        result.setExternalAppointmentsAllowed(diaryOpeningHoursVo.getExternalAppointmentsAllowed());
        LOG.debug(OUTPUT, result);
        return result;
    }

    /**
     *
     * @param occupationTimeOfDiary rangos horarios de atención para una agenda particular (preexistente)
     * @param startDate fecha de inicio definida para nueva agenda
     * @param endDate fecha de fin definida para nueva agenda
     * @return rangos horarios en los que la nueva agenda se superpone con lo definido por la agenda preexistente
     */
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

    /**
     *
     * @param rangeStart1 fecha de comienzo para rango 1
     * @param rangeEnd1 fecha de fin para rango 1
     * @param rangeStart2 fecha de comienzo para rango 2
     * @param rangeEnd2 fecha de fin para rango 2
     * @return lista con todos los identificadores de días de semana superpuestos entre dos rangos de fecha
     */
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

    /**
     *
     * @param openingHours rangos de horarios para un mismo día de semana ordenados
     * @return lista acotada de {@code openingHours} uniendo rangos de tiempo superpuestos
     */
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

}
