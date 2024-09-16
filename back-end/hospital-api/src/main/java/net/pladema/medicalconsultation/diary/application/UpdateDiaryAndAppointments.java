package net.pladema.medicalconsultation.diary.application;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.UpdateAppointmentOpeningHours;
import net.pladema.medicalconsultation.appointment.application.port.AppointmentPort;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.diary.application.port.output.DiaryPort;
import net.pladema.medicalconsultation.diary.domain.UpdateDiaryBo;
import net.pladema.medicalconsultation.diary.domain.UpdateDiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryLabelBo;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateDiaryAndAppointments {

    private final DiaryService diaryService;
    private final DiaryOpeningHoursService diaryOpeningHoursService;
    private final AppointmentService appointmentService;
    private final UpdateAppointmentOpeningHours updateAppointmentOpeningHours;
    private final DiaryPort diaryPort;
    private final AppointmentPort appointmentPort;

    @Transactional
    public Integer run(UpdateDiaryBo diaryToUpdate) {
        log.debug("Input parameters -> diaryToUpdate {}", diaryToUpdate);

        this.assertContextValid(diaryToUpdate);

        Integer diaryToUpdateId = diaryToUpdate.getId();

        diaryToUpdate.updateMyDiaryOpeningHours();

        List<UpdateDiaryAppointmentBo> appointments = diaryPort.getUpdateDiaryAppointments(diaryToUpdateId);

        diaryService.persistDiary(diaryToUpdate);

        appointments.stream()
                .filter(a -> !diaryToUpdate.tryAdjustAppointmentToDiaryOpeningHours(a))
                .forEach(this::updateToOutOfDiaryState);

        updateAppointmentOpeningHours.run(diaryToUpdate);

        diaryService.setDiaryLabels(diaryToUpdate);
        deleteDiaryLabels(diaryToUpdate);

        log.trace("Diary updated -> {}", diaryToUpdate);
        log.debug("Output -> diared updated id {}", diaryToUpdateId);
        return diaryToUpdateId;
    }

    private void assertContextValid(UpdateDiaryBo diaryToUpdate) {
        diaryToUpdate.validateSelf();
        this.validateOverlapWithOccupation(diaryToUpdate);
    }

    private void validateOverlapWithOccupation(UpdateDiaryBo diaryToUpdate) {
        Integer doctorsOfficeSaved = diaryPort.findDoctorsOfficeByDiaryId(diaryToUpdate.getId())
                .orElseThrow(() -> new DiaryException(DiaryEnumException.DOCTORS_OFFICE_NOT_FOUND, "No se ha encontrado un consultorio asociado a la agenda"));

        boolean doctorsOfficeHasChanged = !doctorsOfficeSaved.equals(diaryToUpdate.getDoctorsOfficeId());
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

    private void updateToOutOfDiaryState(UpdateDiaryAppointmentBo appointmentOutOfDiary) {
        if (AppointmentState.BLOCKED == appointmentOutOfDiary.getStateId()) {
            appointmentPort.deleteAppointmentById(appointmentOutOfDiary.getId());
            return;
        }
        if (appointmentOutOfDiary.isScheduledForTheFuture())
            appointmentService.updateState(appointmentOutOfDiary.getId(), AppointmentState.OUT_OF_DIARY, UserInfo.getCurrentAuditor(), "Fuera de agenda");
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
