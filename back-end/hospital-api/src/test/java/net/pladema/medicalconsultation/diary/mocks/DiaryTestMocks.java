package net.pladema.medicalconsultation.diary.mocks;

import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

import java.time.LocalDate;
import java.time.LocalTime;

public class DiaryTestMocks {

    public static Diary createDiary(Integer healthcareProfessionalId, Integer doctorsOfficeId,
                                    LocalDate startDate, LocalDate endDate, Short appointmentDuration,
                                    Boolean automaticRenewal, Short daysBeforeRenew, Boolean professionalAssignShift,
                                    Boolean includeHoliday, Boolean active, Integer clinicalSpecialtyId) {
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
		result.setClinicalSpecialtyId(clinicalSpecialtyId);
        return result;
    }

    public static DoctorsOffice createDoctorsOffice(Integer institutionId, Integer sectorId,
                                                    String description, LocalTime openingTime, LocalTime closingTime) {
        DoctorsOffice result = new DoctorsOffice();
        result.setInstitutionId(institutionId);
        result.setSectorId(sectorId);
        result.setDescription(description);
        result.setOpeningTime(openingTime);
        result.setClosingTime(closingTime);
        return result;
    }

	public static ClinicalSpecialty createClinicalSpecialty(Short clinicalSpecialtyTypeId, String name, String sctIdCode) {
		ClinicalSpecialty result = new ClinicalSpecialty();
		result.setClinicalSpecialtyTypeId(clinicalSpecialtyTypeId);
		result.setName(name);
		result.setSctidCode(sctIdCode);
		return result;
	}

}
