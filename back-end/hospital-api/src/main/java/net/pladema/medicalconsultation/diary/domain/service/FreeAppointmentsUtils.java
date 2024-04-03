package net.pladema.medicalconsultation.diary.domain.service;

import ar.lamansys.sgx.shared.dates.repository.entity.EDayOfWeek;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.diary.domain.FreeAppointmentSearchFilterBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;

@Service
public class FreeAppointmentsUtils {

	public boolean isOpeningHoursValidToReassign(FreeAppointmentSearchFilterBo filter, DiaryOpeningHoursBo diaryOpeningHoursBo) {
		return ((diaryOpeningHoursBo.getOnSiteAttentionAllowed() && filter.getModality().getId().equals(EAppointmentModality.ON_SITE_ATTENTION.getId())) ||
				(diaryOpeningHoursBo.getPatientVirtualAttentionAllowed() && filter.getModality().getId().equals(EAppointmentModality.PATIENT_VIRTUAL_ATTENTION.getId())) ||
				(diaryOpeningHoursBo.getSecondOpinionVirtualAttentionAllowed() && filter.getModality().getId().equals(EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION.getId()))) &&
				(!filter.isMustBeProtected() || ((diaryOpeningHoursBo.getProtectedAppointmentsAllowed() != null && diaryOpeningHoursBo.getProtectedAppointmentsAllowed()) ||
						(diaryOpeningHoursBo.getRegulationProtectedAppointmentsAllowed() != null && diaryOpeningHoursBo.getRegulationProtectedAppointmentsAllowed())));
	}

	public int parseOpeningHoursWeekOfDay(OpeningHoursBo openingHours) {
		return openingHours.getDayWeekId().equals(EDayOfWeek.SUNDAY.getId()) ? DayOfWeek.SUNDAY.getValue() : openingHours.getDayWeekId();
	}

}
