package net.pladema.medicalconsultation.diary.service.impl;

import net.pladema.medicalconsultation.diary.repository.DiaryOpeningHoursRepository;
import net.pladema.medicalconsultation.diary.repository.OpeningHoursRepository;
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
import net.pladema.sgx.dates.repository.entity.EDayOfWeek;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class DiaryOpeningHoursServiceImpl implements DiaryOpeningHoursService {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryOpeningHoursServiceImpl.class);

    private final DiaryOpeningHoursRepository diaryOpeningHoursRepository;

    private final OpeningHoursRepository openingHoursRepository;

    private final DiaryBoMapper diaryBoMapper;

    public DiaryOpeningHoursServiceImpl(DiaryOpeningHoursRepository diaryOpeningHoursRepository,
                                        OpeningHoursRepository openingHoursRepository,
                                        DiaryBoMapper diaryBoMapper) {
        super();
        this.diaryOpeningHoursRepository = diaryOpeningHoursRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.diaryBoMapper = diaryBoMapper;
    }

    @Override
    public void load(Integer diaryId, List<DiaryOpeningHoursBo> diaryOpeningHours) {
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
            else
                openingHoursId = openingHoursRepository.save(newOpeningHours).getId();

            openingHoursBo.setId(openingHoursId);
            diaryOpeningHoursRepository.save(createDiaryOpeningHoursInstance(diaryId, openingHoursId, doh));

        });
    }

    private DiaryOpeningHours createDiaryOpeningHoursInstance(Integer diaryId, Integer openingHoursId, DiaryOpeningHoursBo doh){
        DiaryOpeningHours diaryOpeningHours = new DiaryOpeningHours();
        diaryOpeningHours.setPk(new DiaryOpeningHoursPK(diaryId, openingHoursId));
        diaryOpeningHours.setMedicalAttentionTypeId(doh.getMedicalAttentionTypeId());
        diaryOpeningHours.setOverturnCount(doh.getOverturnCount());
        return diaryOpeningHours;
    }

    @Override
    public List<OccupationBo> findAllWeeklyDoctorsOfficeOccupation(Integer doctorOfficeId, LocalDate newDiarystart, LocalDate newDiaryEnd){
        LOG.debug("Input parameters -> doctorOfficeId {}, startDate {}, endDate {}",
                doctorOfficeId, newDiarystart, newDiaryEnd);

        List<OccupationVo> queryResults = diaryOpeningHoursRepository
                .findAllWeeklyDoctorsOfficeOcupation(doctorOfficeId, newDiarystart, newDiaryEnd);

        //Se consideran únicamente los horarios de agendas alcanzados (según calendario)
        // para el rango de fecha newDiarystart y newDiaryEnd
        List<OpeningHours> validQueryResults = new ArrayList<>();
        queryResults.stream()
                .collect(Collectors.groupingBy(OccupationVo::getDiaryId))
                .forEach( (diaryId,occupationTimeOfDiary)->
                        validQueryResults.addAll(
                            getOnlyDiaryOverlappingDays(occupationTimeOfDiary, newDiarystart, newDiaryEnd))
                    );

        List<OccupationBo> result = new ArrayList<>();
        validQueryResults.stream()
                .collect(Collectors.groupingBy(OpeningHours::getDayWeekId))
                .forEach( (dayWeekId,openingHours)->
                    result.add(mergeRangeTimeOfOpeningHours(dayWeekId, openingHours))
                );
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
    private List<Short> overlappingDays(LocalDate rangeStart1, LocalDate rangeEnd1,
                                        LocalDate rangeStart2, LocalDate rangeEnd2){
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
                .comparing(OpeningHours::getFrom, LocalTime::compareTo);
        openingHours = openingHours.stream().sorted(ascendingOrder).collect(toList());

        List<TimeRangeBo> timeRanges = new ArrayList<>();

        TimeRangeBo lastTimeRange = new TimeRangeBo(openingHours.get(0));
        for(int index = 1; index < openingHours.size(); index ++){
            OpeningHours current = openingHours.get(index);
            if(current.getFrom().isAfter(lastTimeRange.getTo())) {
                timeRanges.add(lastTimeRange);
                lastTimeRange = new TimeRangeBo(current);
            }
            else {
                lastTimeRange.setTo(current.getTo());
            }
        }
        timeRanges.add(lastTimeRange);

        OccupationBo dayOpeningHours = new OccupationBo();
        dayOpeningHours.setDayWeek(EDayOfWeek.map(dayWeekId));
        dayOpeningHours.setTimeRanges(timeRanges);
        return dayOpeningHours;
    }
}
