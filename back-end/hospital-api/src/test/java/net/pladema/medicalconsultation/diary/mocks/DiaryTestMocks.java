package net.pladema.medicalconsultation.diary.mocks;

import net.pladema.medicalconsultation.diary.repository.entity.Diary;

import java.time.LocalDate;

public class DiaryTestMocks {

    public static Diary createDiary(Integer healthcareProfessionalId, Integer doctorsOfficeId,
                                    LocalDate startDate, LocalDate endDate, Short appointmentDuration,
                                    Boolean automaticRenewal, Short daysBeforeRenew, Boolean professionalAssignShift,
                                    Boolean includeHoliday, Boolean active) {
        Diary result = new Diary();
        result.setHealthcareProfessionalId(healthcareProfessionalId);
        result.setDoctorsOfficeId(doctorsOfficeId);
        result.setStartDate(startDate);
        result.setEndDate(endDate);
        result.setAppointmentDuration(appointmentDuration);
        result.setAutomaticRenewal(automaticRenewal);
        result.setDaysBeforeRenew(daysBeforeRenew);
        result.setProfessionalAsignShift(professionalAssignShift);
        result.setIncludeHoliday(includeHoliday);
        result.setActive(active);
        return result;
    }


}
