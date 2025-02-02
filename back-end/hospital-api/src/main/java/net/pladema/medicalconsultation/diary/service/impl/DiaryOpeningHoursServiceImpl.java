package net.pladema.medicalconsultation.diary.service.impl;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgx.shared.dates.repository.entity.EDayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.repository.DiaryOpeningHoursRepository;
import net.pladema.medicalconsultation.diary.repository.OpeningHoursRepository;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo;
import net.pladema.medicalconsultation.diary.repository.domain.OccupationVo;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHours;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHoursPK;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;
import net.pladema.medicalconsultation.diary.service.DiaryBoMapper;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.TimeRangeBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryOpeningHoursServiceImpl implements DiaryOpeningHoursService {

    public static final String OUTPUT = "Output -> {}";

    private final DiaryOpeningHoursRepository diaryOpeningHoursRepository;
    private final OpeningHoursRepository openingHoursRepository;
    private final DiaryBoMapper diaryBoMapper;
    private final SharedReferenceCounterReference sharedReferenceCounterReference;

    @Override
    @Transactional
    public void update(Integer diaryId, List<DiaryOpeningHoursBo> diaryOpeningHours) {
        Sort sort = Sort.by("dayWeekId", "from");

        Map<OpeningHours, Integer> openingHoursMap = createUniqueOpeningHoursMap(sort);

        Set<DiaryOpeningHoursPK> newPKs = diaryOpeningHours.stream()
                .peek(doh -> setOpeningHours(doh, openingHoursMap))
                .map(doh -> new DiaryOpeningHoursPK(diaryId, doh.getOpeningHours().getId()))
                .collect(Collectors.toSet());

        diaryOpeningHoursRepository.deleteAllByDiaryIdAndNotInPK(diaryId, newPKs);

        this.updateOrSaveAssociations(diaryId, diaryOpeningHours);
    }

    private Map<OpeningHours, Integer> createUniqueOpeningHoursMap(Sort sort) {
        List<OpeningHours> allOpeningHours = openingHoursRepository.findAll(sort);

        Set<OpeningHours> uniqueOpeningHoursSet = new HashSet<>(allOpeningHours);

        return uniqueOpeningHoursSet.stream()
                .collect(Collectors.toMap(Function.identity(), OpeningHours::getId));
    }

    private void setOpeningHours(DiaryOpeningHoursBo diaryOpeningHoursBo, Map<OpeningHours, Integer> openingHoursMap) {
        OpeningHoursBo openingHoursBo = diaryOpeningHoursBo.getOpeningHours();
        OpeningHours newOpeningHours = diaryBoMapper.toOpeningHours(openingHoursBo);

        Integer openingHoursId = openingHoursMap.get(newOpeningHours);
        if (openingHoursId == null) {
            openingHoursId = openingHoursRepository.save(newOpeningHours).getId();
        }
        openingHoursBo.setId(openingHoursId);
    }

    private void updateOrSaveAssociations(Integer diaryId, List<DiaryOpeningHoursBo> diaryOpeningHours) {
        diaryOpeningHours.forEach(diaryOpeningHoursBo -> updateDiaryWithOpeningHoursIfNecessary(diaryId, diaryOpeningHoursBo));
    }

    private void updateDiaryWithOpeningHoursIfNecessary(Integer diaryId, DiaryOpeningHoursBo diaryOpeningHoursBo) {
        Optional<DiaryOpeningHours> persistedEntity = diaryOpeningHoursRepository.findById(new DiaryOpeningHoursPK(diaryId, diaryOpeningHoursBo.getOpeningHoursId()));

        if (persistedEntity.isEmpty()) {
            DiaryOpeningHours newDiaryOpeningHours = createDiaryOpeningHoursInstance(diaryId, diaryOpeningHoursBo);
            diaryOpeningHoursRepository.saveAndFlush(newDiaryOpeningHours);
            return;
        }

        persistedEntity.filter(existingEntity -> shouldUpdateDiaryOpeningHours(existingEntity, diaryOpeningHoursBo))
                .ifPresent(existingEntity -> {
                            setValues(existingEntity, diaryOpeningHoursBo);
                            diaryOpeningHoursRepository.saveAndFlush(existingEntity);
                });
    }

    private boolean shouldUpdateDiaryOpeningHours(DiaryOpeningHours existingEntity, DiaryOpeningHoursBo doh) {
        return !(Objects.equals(existingEntity.getMedicalAttentionTypeId(), doh.getMedicalAttentionTypeId())
                && Objects.equals(existingEntity.getOverturnCount(), doh.getOverturnCount())
                && Objects.equals(existingEntity.getExternalAppointmentsAllowed(), doh.getExternalAppointmentsAllowed())
                && Objects.equals(existingEntity.getProtectedAppointmentsAllowed(), doh.getProtectedAppointmentsAllowed())
                && Objects.equals(existingEntity.getOnSiteAttentionAllowed(), doh.getOnSiteAttentionAllowed())
                && Objects.equals(existingEntity.getPatientVirtualAttentionAllowed(), doh.getPatientVirtualAttentionAllowed())
                && Objects.equals(existingEntity.getSecondOpinionVirtualAttentionAllowed(), doh.getSecondOpinionVirtualAttentionAllowed())
                && Objects.equals(existingEntity.getRegulationProtectedAppointmentsAllowed(), doh.getRegulationProtectedAppointmentsAllowed()));
    }

    private DiaryOpeningHours createDiaryOpeningHoursInstance(Integer diaryId, DiaryOpeningHoursBo doh) {
        DiaryOpeningHours diaryOpeningHours = new DiaryOpeningHours();
        diaryOpeningHours.setPk(new DiaryOpeningHoursPK(diaryId, doh.getOpeningHoursId()));
        setValues(diaryOpeningHours, doh);
        return diaryOpeningHours;
    }

    private static void setValues(DiaryOpeningHours entity, DiaryOpeningHoursBo doh) {
        entity.setMedicalAttentionTypeId(doh.getMedicalAttentionTypeId());
        entity.setOverturnCount((doh.getOverturnCount() != null) ? doh.getOverturnCount() : 0);
        entity.setExternalAppointmentsAllowed(doh.getExternalAppointmentsAllowed());
        entity.setProtectedAppointmentsAllowed(doh.getProtectedAppointmentsAllowed());
        entity.setOnSiteAttentionAllowed(doh.getOnSiteAttentionAllowed());
        entity.setPatientVirtualAttentionAllowed(doh.getPatientVirtualAttentionAllowed());
        entity.setSecondOpinionVirtualAttentionAllowed(doh.getSecondOpinionVirtualAttentionAllowed());
        entity.setRegulationProtectedAppointmentsAllowed(doh.getRegulationProtectedAppointmentsAllowed());
    }

    @Override
    public List<OccupationBo> findAllWeeklyDoctorsOfficeOccupation(Integer doctorOfficeId,
                                                                   LocalDate newDiaryStart,
                                                                   LocalDate newDiaryEnd,
                                                                   Integer ignoreDiaryId) throws DiaryOpeningHoursException {
        log.debug("Input parameters -> doctorOfficeId {}, startDate {}, endDate {}",
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
                .forEach((diaryId, occupationTimeOfDiary) ->
                        validQueryResults.addAll(
                                getOnlyDiaryOverlappingDays(occupationTimeOfDiary, newDiaryStart, newDiaryEnd))
                );

        List<OccupationBo> result = new ArrayList<>();
        validQueryResults.stream()
                .collect(Collectors.groupingBy(OpeningHours::getDayWeekId))
                .forEach((dayWeekId, openingHours) ->
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
        log.debug("Input parameters -> ignoreDiaryId {}", ignoreDiaryId);
        Predicate<OccupationVo> result = (ignoreDiaryId == null) ? e -> true : e -> !e.getDiaryId().equals(ignoreDiaryId);
        log.debug(OUTPUT, result);
        return result;
    }


    @Override
    public Collection<DiaryOpeningHoursBo> getDiariesOpeningHours(List<Integer> diaryIds) {
        log.debug("Input parameters -> diaryIds {} ", diaryIds);
        Collection<DiaryOpeningHoursBo> result = new ArrayList<>();
        if (!diaryIds.isEmpty()) {
            List<DiaryOpeningHoursVo> resultQuery = diaryOpeningHoursRepository.getDiariesOpeningHours(diaryIds);
            result = resultQuery.stream().map(this::createDiaryOpeningHoursBo).collect(Collectors.toList());
        }
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Collection<DiaryOpeningHoursBo> getDiaryOpeningHours(Integer diaryId) {
        log.debug("Input parameters -> diaryId {} ", diaryId);
        Collection<DiaryOpeningHoursBo> result = new ArrayList<>();
        List<DiaryOpeningHoursVo> resultQuery = diaryOpeningHoursRepository.getDiaryOpeningHours(diaryId);
        result = resultQuery.stream().map(this::createDiaryOpeningHoursBo).collect(Collectors.toList());
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public boolean hasProtectedAppointments(Integer openingHourId) {
        log.debug("Input parameters -> openingHourId {} ", openingHourId);
        return sharedReferenceCounterReference.existsProtectedAppointmentInOpeningHour(openingHourId);
    }

    @Override
    public Collection<DiaryOpeningHoursBo> getDiariesOpeningHoursByMedicalAttentionType(List<Integer> diaryIds, short medicalAttentionTypeId) {
        log.debug("Input parameters -> diaryIds {}, medicalAttentionTypeId {} ", diaryIds, medicalAttentionTypeId);
        Collection<DiaryOpeningHoursBo> result = new ArrayList<>();
        if (!diaryIds.isEmpty()) {
            List<DiaryOpeningHoursVo> resultQuery = diaryOpeningHoursRepository.getDiariesOpeningHoursByMedicalAttentionType(diaryIds, medicalAttentionTypeId);
            result = resultQuery.stream().map(this::createDiaryOpeningHoursBo).collect(Collectors.toList());
        }
        log.debug(OUTPUT, result);
        return result;
    }

    private DiaryOpeningHoursBo createDiaryOpeningHoursBo(DiaryOpeningHoursVo diaryOpeningHoursVo) {
        log.debug("Input parameters -> diaryOpeningHoursVo {} ", diaryOpeningHoursVo);
        DiaryOpeningHoursBo result = new DiaryOpeningHoursBo();
        result.setDiaryId(diaryOpeningHoursVo.getDiaryId());
        result.setMedicalAttentionTypeId(diaryOpeningHoursVo.getMedicalAttentionTypeId());
        result.setOverturnCount(diaryOpeningHoursVo.getOverturnCount());
        result.setOpeningHours(new OpeningHoursBo(diaryOpeningHoursVo.getOpeningHours()));
        result.setExternalAppointmentsAllowed(diaryOpeningHoursVo.getExternalAppointmentsAllowed());
        result.setProtectedAppointmentsAllowed(diaryOpeningHoursVo.getProtectedAppointmentsAllowed());
        result.setOnSiteAttentionAllowed(diaryOpeningHoursVo.getOnSiteAttentionAllowed());
        result.setPatientVirtualAttentionAllowed(diaryOpeningHoursVo.getPatientVirtualAttentionAllowed());
        result.setSecondOpinionVirtualAttentionAllowed(diaryOpeningHoursVo.getSecondOpinionVirtualAttentionAllowed());
        result.setRegulationProtectedAppointmentsAllowed(diaryOpeningHoursVo.getRegulationProtectedAppointmentsAllowed());
        log.debug(OUTPUT, result);
        return result;
    }

    /**
     * @param occupationTimeOfDiary rangos horarios de atención para una agenda particular (preexistente)
     * @param startDate             fecha de inicio definida para nueva agenda
     * @param endDate               fecha de fin definida para nueva agenda
     * @return rangos horarios en los que la nueva agenda se superpone con lo definido por la agenda preexistente
     */
    private List<OpeningHours> getOnlyDiaryOverlappingDays(List<OccupationVo> occupationTimeOfDiary,
                                                           LocalDate startDate, LocalDate endDate) {
        LocalDate diaryStart = occupationTimeOfDiary.get(0).getStartDate();
        LocalDate diaryEnd = occupationTimeOfDiary.get(0).getEndDate();
        List<Short> overlappingDays = overlappingDays(diaryStart, diaryEnd, startDate, endDate);
        return occupationTimeOfDiary.stream()
                .map(OccupationVo::getOpeningHours)
                .filter(oh -> overlappingDays.contains(oh.getDayWeekId()))
                .collect(Collectors.toList());
    }

    /**
     * @param rangeStart1 fecha de comienzo para rango 1
     * @param rangeEnd1   fecha de fin para rango 1
     * @param rangeStart2 fecha de comienzo para rango 2
     * @param rangeEnd2   fecha de fin para rango 2
     * @return lista con todos los identificadores de días de semana superpuestos entre dos rangos de fecha
     */
    @Override
    public List<Short> overlappingDays(@NotNull LocalDate rangeStart1, @NotNull LocalDate rangeEnd1,
                                       @NotNull LocalDate rangeStart2, @NotNull LocalDate rangeEnd2) {
        List<Short> validDaysWeek = new ArrayList<>();
        LocalDate start = rangeStart1.isBefore(rangeStart2) ? rangeStart2 : rangeStart1;
        LocalDate end = rangeEnd1.isBefore(rangeStart2) ? rangeEnd1 : rangeEnd2;
        int overlappingDays = end.getDayOfYear() - start.getDayOfYear() + 1;
        if (overlappingDays < 7) {
            while (!start.isEqual(end)) {
                validDaysWeek.add((short) start.getDayOfWeek().getValue());
                start = start.plusDays(1L);
            }
            validDaysWeek.add((short) start.getDayOfWeek().getValue());
        } else
            validDaysWeek.addAll(EDayOfWeek.getAllIds());
        return validDaysWeek;
    }

    /**
     * @param openingHours rangos de horarios para un mismo día de semana ordenados
     * @return lista acotada de {@code openingHours} uniendo rangos de tiempo superpuestos
     */
    private OccupationBo mergeRangeTimeOfOpeningHours(Short dayWeekId, @NotEmpty List<OpeningHours> openingHours) {
        Comparator<OpeningHours> ascendingOrder = Comparator
                .comparing(OpeningHours::getFrom, LocalTime::compareTo)
                .thenComparing(OpeningHours::getTo, LocalTime::compareTo);
        openingHours = openingHours.stream().sorted(ascendingOrder).collect(toList());

        List<TimeRangeBo> timeRanges = new ArrayList<>();

        TimeRangeBo lastTimeRange = new TimeRangeBo(openingHours.get(0));
        for (int index = 1; index < openingHours.size(); index++) {
            OpeningHours current = openingHours.get(index);
            if (current.getFrom().isAfter(lastTimeRange.getTo())) {
                timeRanges.add(lastTimeRange);
                lastTimeRange = new TimeRangeBo(current);
            } else if (current.getTo().isAfter(lastTimeRange.getTo()))
                lastTimeRange.setTo(current.getTo());
        }
        timeRanges.add(lastTimeRange);

        OccupationBo dayOpeningHours = new OccupationBo();
        dayOpeningHours.setDayWeek(EDayOfWeek.map(dayWeekId));
        dayOpeningHours.setTimeRanges(timeRanges);
        return dayOpeningHours;
    }

}
