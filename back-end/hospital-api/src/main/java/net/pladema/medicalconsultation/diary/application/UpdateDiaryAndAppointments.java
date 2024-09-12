package net.pladema.medicalconsultation.diary.application;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.UpdateAppointmentOpeningHoursService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.application.port.output.DiaryPort;
import net.pladema.medicalconsultation.diary.domain.UpdateDiaryBo;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryLabelBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateDiaryAndAppointments {

    private final HandleDiaryOutOfBoundsAppointments handleDiaryOutOfBoundsAppointments;
    private final DiaryService diaryService;
    private final DiaryOpeningHoursService diaryOpeningHoursService;
    private final AppointmentService appointmentService;
    private final UpdateAppointmentOpeningHoursService updateApmtOHService;
    private final DiaryPort diaryPort;

    @Transactional
    public Integer run(UpdateDiaryBo diaryToUpdate) {
        log.debug("Input parameters -> diaryToUpdate {}", diaryToUpdate);

        diaryToUpdate.validateSelf();

        DiaryBo diarySaved = diaryPort.findById(diaryToUpdate.getId())
                .orElseThrow(() -> new NotFoundException("diary-not-found", "diary.invalid.id"));

        this.validateOverlapWithOccupation(diaryToUpdate, diarySaved);

        diaryToUpdate.setId(diarySaved.getId());
        diaryToUpdate.updateMyDiaryOpeningHours();

        List<UpdateDiaryAppointmentBo> appointments = diaryPort.getUpdateDiaryAppointments(diaryToUpdate.getId());

        handleDiaryOutOfBoundsAppointments.run(diaryToUpdate, appointments);

        appointments.forEach(diaryToUpdate::adjustAppointmentToDiaryOpeningHours);

        diaryService.persistDiary(diaryToUpdate);

        // re-define
        //updatedExistingAppointments(diaryToUpdate, apmtsByNewDOH);
        diaryService.setDiaryLabels(diaryToUpdate);
        deleteDiaryLabels(diaryToUpdate);

        Integer result = diaryToUpdate.getId();

        log.trace("Diary updated -> {}", diaryToUpdate);
        log.debug("Output -> result {}", result);
        return result;
    }

    private void validateOverlapWithOccupation(UpdateDiaryBo diaryToUpdate, DiaryBo diarySaved) {
        boolean doctorsOfficeHasChanged = !diaryToUpdate.equalsDoctorsOffice(diarySaved);
        if (doctorsOfficeHasChanged && foundOverlapWithOccupation(diaryToUpdate))
            throw new DiaryException(DiaryEnumException.DIARY_OPENING_HOURS_OVERLAP, "Superposición de rango horario en consultorio");
    }

    private Boolean foundOverlapWithOccupation(UpdateDiaryBo diaryBo) {
        List<OpeningHoursBo> allOpeningHoursDoctorsOfficeOccupation;
        try {
            allOpeningHoursDoctorsOfficeOccupation = diaryOpeningHoursService.findAllWeeklyDoctorsOfficeOccupation(diaryBo.getDoctorsOfficeId(), diaryBo.getStartDate(), diaryBo.getEndDate(), null)
                    .stream()
                    .flatMap(occupationBo -> occupationBo.getTimeRanges()
                            .stream()
                            .map(timeRangeBo -> new OpeningHoursBo(occupationBo.getId(), timeRangeBo)))
                    .collect(toList());
        } catch (DiaryOpeningHoursException e) {
            throw new DiaryException(DiaryEnumException.DIARY_OPENING_HOURS_OVERLAP, e.getMessage());
        }

        return diaryBo.getUpdateDiaryOpeningHours()
                .stream()
                .map(UpdateDiaryOpeningHoursBo::getOpeningHours)
                .flatMap(openingHoursFromNewDiary -> allOpeningHoursDoctorsOfficeOccupation.stream()
                                                        .filter(openingHoursOccupation -> openingHoursOccupation.isOverlapWithOccupation(openingHoursFromNewDiary))
                )
                .findAny()
                .isPresent();
    }

    private void updatedExistingAppointments(DiaryBo diaryToUpdate,
                                             HashMap<DiaryOpeningHoursBo, List<AppointmentBo>> apmtsByNewDOH) {
        Collection<DiaryOpeningHoursBo> dohSavedList = diaryOpeningHoursService
                .getDiariesOpeningHours(List.of(diaryToUpdate.getId()));

        apmtsByNewDOH.forEach(
                (doh, apmts) -> dohSavedList.stream()
                                    .filter(doh::equals)
                                    .findAny()
                                    .ifPresent(savedDoh -> apmts.forEach(apmt -> apmt.setOpeningHoursId(savedDoh.getOpeningHoursId())))
        );

        List<AppointmentBo> apmtsToUpdate = apmtsByNewDOH.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(toList());

        apmtsToUpdate.forEach(appointment -> updateApmtOHService.execute(appointment, false));
    }

    private void deleteDiaryLabels(DiaryBo diaryToUpdate) {
        List<Integer> ids = diaryToUpdate.getDiaryLabelBo()
                .stream()
                .map(DiaryLabelBo::getId)
                .collect(Collectors.toList());
        ids.add(-1);
        appointmentService.deleteLabelFromAppointment(diaryToUpdate.getId(), ids);
        diaryPort.deleteDiaryLabels(diaryToUpdate.getId(), ids);
    }
}
