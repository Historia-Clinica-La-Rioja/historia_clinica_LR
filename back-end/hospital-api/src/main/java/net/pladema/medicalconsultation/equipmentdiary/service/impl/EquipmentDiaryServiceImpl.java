package net.pladema.medicalconsultation.equipmentdiary.service.impl;


import ar.lamansys.sgx.shared.dates.repository.entity.EDayOfWeek;
import net.pladema.medicalconsultation.appointment.application.UpdateEquipmentAppointmentOpeningHours;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.OverturnsLimitException;
import net.pladema.medicalconsultation.equipmentdiary.repository.EquipmentDiaryRepository;
import net.pladema.medicalconsultation.equipmentdiary.repository.domain.CompleteEquipmentDiaryListVo;
import net.pladema.medicalconsultation.equipmentdiary.repository.domain.EquipmentDiaryListVo;
import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiary;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryOpeningHoursService;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.CompleteEquipmentDiaryBo;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryOpeningHoursBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ar.lamansys.sgx.shared.dates.utils.DateUtils.getWeekDay;
import static ar.lamansys.sgx.shared.dates.utils.DateUtils.isBetween;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class EquipmentDiaryServiceImpl implements EquipmentDiaryService {

    private static final String INPUT_DIARY_ID = "Input parameters -> diaryId {}";
    private static final String OUTPUT = "Output -> {}";

    private final EquipmentDiaryOpeningHoursService equipmentDiaryOpeningHoursService;
    private final EquipmentDiaryRepository equipmentDiaryRepository;
    private final AppointmentService appointmentService;
    private final UpdateEquipmentAppointmentOpeningHours updateEquipmentAppointmentOpeningHours;

    @Override
    @Transactional
    public Integer addDiary(EquipmentDiaryBo equipmentDiaryToSave) {
        log.debug("Input parameters -> equipmentDiaryToSave {}", equipmentDiaryToSave);
        EquipmentDiary equipmentDiary = createDiaryInstance(equipmentDiaryToSave);
        Integer diaryId = persistDiary(equipmentDiaryToSave, equipmentDiary);

        equipmentDiaryToSave.setId(diaryId);
        log.debug("EquipmentDiary saved -> {}", equipmentDiaryToSave);
        return diaryId;
    }

    private Integer persistDiary(EquipmentDiaryBo equipmentDiaryToSave, EquipmentDiary equipmentDiary) {
        equipmentDiary = equipmentDiaryRepository.save(equipmentDiary);
        Integer equipmentDiaryId = equipmentDiary.getId();
        equipmentDiaryOpeningHoursService.update(equipmentDiaryId, equipmentDiaryToSave.getDiaryOpeningHours());
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
        log.debug(
                "Input parameters -> equipmentId {}, newDiaryStart {}, newDiaryEnd {}, appointmentDuration{}",
                equipmentId, newDiaryStart, newDiaryEnd, appointmentDuration);
        List<Integer> diaryIds = excludeDiaryId.isPresent()
                ? equipmentDiaryRepository.findAllOverlappingDiaryByEquipmentExcludingDiary(equipmentId,
                newDiaryStart, newDiaryEnd, appointmentDuration, excludeDiaryId.get())
                : equipmentDiaryRepository.findAllOverlappingDiaryByEquipment(equipmentId,
                newDiaryStart, newDiaryEnd, appointmentDuration);
        log.debug("EquipmentDiary saved -> {}", diaryIds);
        return diaryIds;

    }

    @Override
    public List<EquipmentDiaryBo> getAllOverlappingDiary(@NotNull Integer equipmentId,
                                                         @NotNull LocalDate newDiaryStart, @NotNull LocalDate newDiaryEnd, Optional<Integer> excludeDiaryId) {
        log.debug(
                "Input parameters -> equipmentId {}, newDiaryStart {}, newDiaryEnd {}",
                equipmentId, newDiaryStart, newDiaryEnd);
        List<EquipmentDiary> diaries = excludeDiaryId.isPresent()
                ? equipmentDiaryRepository.findAllOverlappingDiaryExcludingDiary(equipmentId,
                newDiaryStart, newDiaryEnd, excludeDiaryId.get())
                : equipmentDiaryRepository.findAllOverlappingDiary(equipmentId, newDiaryStart,
                newDiaryEnd);
        List<EquipmentDiaryBo> result = diaries.stream().map(this::createEquipmentDiaryBoInstance).collect(Collectors.toList());
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<EquipmentDiaryBo> getEquipmentDiariesFromEquipment(Integer equipmentId, Boolean active) {
        log.debug("Input parameters -> equipmentId {}, active {}", equipmentId, active);
        List<EquipmentDiary> equipmentDiaries = equipmentDiaryRepository.getEquipmentDiariesFromEquipment(equipmentId, active);
        List<EquipmentDiaryBo> result = equipmentDiaries.stream().map(this::createEquipmentDiaryBoInstance).collect(Collectors.toList());
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Optional<CompleteEquipmentDiaryBo> getEquipmentDiary(Integer equipmentDiaryId) {
        log.debug(INPUT_DIARY_ID, equipmentDiaryId);
        Optional<CompleteEquipmentDiaryBo> result = equipmentDiaryRepository.getEquipmentDiary(equipmentDiaryId).map(this::createCompleteEquipmentDiaryBoInstance)
                .map(completeOpeningHours());

        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    @Transactional
    public Integer updateDiary(EquipmentDiaryBo equipmentDiaryToUpdate) {
        log.debug("Input parameters -> equipmentDiaryToUpdate {}", equipmentDiaryToUpdate);

        return equipmentDiaryRepository.findById(equipmentDiaryToUpdate.getId())
                .map(savedEquipmentDiary -> {
                    HashMap<EquipmentDiaryOpeningHoursBo, List<AppointmentBo>> apmtsByNewDOH = new HashMap<>();
                    equipmentDiaryToUpdate.getDiaryOpeningHours().forEach(doh -> {
                        doh.setDiaryId(savedEquipmentDiary.getId());
                        apmtsByNewDOH.put(doh, new ArrayList<>());
                    });
                    Collection<AppointmentBo> apmts = appointmentService.getAppointmentsByEquipmentDiary(equipmentDiaryToUpdate.getId(), equipmentDiaryToUpdate.getStartDate(),
                            equipmentDiaryToUpdate.getEndDate());
                    adjustExistingAppointmentsOpeningHours(apmtsByNewDOH, apmts);
                    persistDiary(equipmentDiaryToUpdate, mapDiaryBo(equipmentDiaryToUpdate, savedEquipmentDiary));
                    updatedExistingAppointments(equipmentDiaryToUpdate, apmtsByNewDOH);
                    log.debug("Diary updated -> {}", equipmentDiaryToUpdate);
                    return equipmentDiaryToUpdate.getId();
                })
                .orElseThrow(() -> new EntityNotFoundException("diary.invalid.id"));

    }

    @Override
    public Optional<EquipmentDiaryBo> getEquipmentDiaryByAppointment(Integer appointmentId) {
        log.debug("Input parameters -> appointmentId {}", appointmentId);
        Optional<EquipmentDiaryBo> result = equipmentDiaryRepository.getDiaryByAppointment(appointmentId).map(this::createDiaryBoInstance);
        log.debug(OUTPUT, result);
        return result;
    }

    private EquipmentDiaryBo createDiaryBoInstance(EquipmentDiary diary) {
        log.debug("Input parameters -> diary {}", diary);
        EquipmentDiaryBo result = new EquipmentDiaryBo();
        result.setId(diary.getId());
        result.setStartDate(diary.getStartDate());
        result.setEndDate(diary.getEndDate());
        result.setAppointmentDuration(diary.getAppointmentDuration());
        result.setAutomaticRenewal(diary.isAutomaticRenewal());
        result.setIncludeHoliday(diary.isIncludeHoliday());
        result.setDeleted(diary.isDeleted());
        log.debug(OUTPUT, result);
        return result;
    }

    private void updatedExistingAppointments(EquipmentDiaryBo diaryToUpdate,
                                             HashMap<EquipmentDiaryOpeningHoursBo, List<AppointmentBo>> apmtsByNewDOH) {
        Collection<EquipmentDiaryOpeningHoursBo> dohSavedList = equipmentDiaryOpeningHoursService
                .getDiariesOpeningHours(Stream.of(diaryToUpdate.getId()).collect(toList()));
        apmtsByNewDOH.forEach((doh, apmts) -> dohSavedList.stream().filter(doh::equals).findAny().ifPresent(
                savedDoh -> apmts.forEach(apmt -> apmt.setOpeningHoursId(savedDoh.getOpeningHours().getId()))));
        List<AppointmentBo> apmtsToUpdate = apmtsByNewDOH.values().stream().flatMap(Collection::stream)
                .collect(toList());
        apmtsToUpdate.forEach(updateEquipmentAppointmentOpeningHours::run);
    }

    private void adjustExistingAppointmentsOpeningHours(HashMap<EquipmentDiaryOpeningHoursBo, List<AppointmentBo>> apmtsByNewDOH,
                                                        Collection<AppointmentBo> apmts) {
        apmtsByNewDOH.forEach((doh, apmtsList) -> {
            apmtsList.addAll(apmts.stream().filter(apmt -> belong(apmt, doh)).collect(toList()));
            if (overturnsOutOfLimit(doh, apmtsList)) {
                throw new OverturnsLimitException(
                        "Se encuentran asignados una cantidad mayor de sobreturnos al límite establecido en la franja del dia " +
                                EDayOfWeek.map(doh.getOpeningHours().getDayWeekId()).getDescription() +
                                ", en el horario de " + doh.getOpeningHours().getFrom() + "hs. a " + doh.getOpeningHours().getTo() + "hs.");
            }
        });
    }

    private boolean belong(AppointmentBo apmt, EquipmentDiaryOpeningHoursBo edoh) {
        return getWeekDay(apmt.getDate()).equals(edoh.getOpeningHours().getDayWeekId())
                && isBetween(apmt.getHour(), edoh.getOpeningHours().getFrom(), edoh.getOpeningHours().getTo());
    }

    private boolean overturnsOutOfLimit(EquipmentDiaryOpeningHoursBo edoh, List<AppointmentBo> apmtsList) {
        Map<LocalDate, Long> overturnsByDate = apmtsList.stream().filter(AppointmentBo::isOverturn)
                .collect(groupingBy(AppointmentBo::getDate, counting()));
        return overturnsByDate.values().stream().anyMatch(overturns -> overturns > edoh.getOverturnCount().intValue());
    }

    private Function<CompleteEquipmentDiaryBo, CompleteEquipmentDiaryBo> completeOpeningHours() {
        return completeDiary -> {
            Collection<EquipmentDiaryOpeningHoursBo> diaryOpeningHours = equipmentDiaryOpeningHoursService
                    .getDiariesOpeningHours(Stream.of(completeDiary.getId()).collect(toList()));
            completeDiary.setDiaryOpeningHours(new ArrayList<>(diaryOpeningHours));
            return completeDiary;
        };
    }

    private EquipmentDiaryBo createEquipmentDiaryBoInstance(EquipmentDiary equipmentDiary) {
        log.debug("Input parameters -> equipmentDiary {}", equipmentDiary);
        EquipmentDiaryBo result = new EquipmentDiaryBo();
        result.setId(equipmentDiary.getId());
        result.setEquipmentId(equipmentDiary.getEquipmentId());
        result.setStartDate(equipmentDiary.getStartDate());
        result.setEndDate(equipmentDiary.getEndDate());
        result.setAppointmentDuration(equipmentDiary.getAppointmentDuration());
        result.setAutomaticRenewal(equipmentDiary.isAutomaticRenewal());
        result.setIncludeHoliday(equipmentDiary.isIncludeHoliday());
        result.setDeleted(equipmentDiary.isDeleted());
        log.debug(OUTPUT, result);
        return result;
    }

    private CompleteEquipmentDiaryBo createCompleteEquipmentDiaryBoInstance(CompleteEquipmentDiaryListVo completeEquipmentDiaryListVo) {
        log.debug("Input parameters -> completeEquipmentDiaryListVo {}", completeEquipmentDiaryListVo);
        CompleteEquipmentDiaryBo result = new CompleteEquipmentDiaryBo(createDiaryBoInstanceFromVO(completeEquipmentDiaryListVo));
        result.setSectorId(completeEquipmentDiaryListVo.getSectorId());
        result.setSectorDescription(completeEquipmentDiaryListVo.getSectorDescription());
        log.debug(OUTPUT, result);
        return result;
    }

    private EquipmentDiaryBo createDiaryBoInstanceFromVO(EquipmentDiaryListVo equipmentDiaryListVo) {
        log.debug("Input parameters -> diaryListVo {}", equipmentDiaryListVo);
        EquipmentDiaryBo result = new EquipmentDiaryBo();
        result.setId(equipmentDiaryListVo.getId());
        result.setStartDate(equipmentDiaryListVo.getStartDate());
        result.setEndDate(equipmentDiaryListVo.getEndDate());
        result.setAppointmentDuration(equipmentDiaryListVo.getAppointmentDuration());
        result.setAutomaticRenewal(equipmentDiaryListVo.isAutomaticRenewal());
        result.setIncludeHoliday(equipmentDiaryListVo.isIncludeHoliday());
        result.setEquipmentId(equipmentDiaryListVo.getEquipmentId());
        log.debug(OUTPUT, result);
        return result;
    }


}
