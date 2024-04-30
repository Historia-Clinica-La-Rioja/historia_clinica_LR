package net.pladema.medicalconsultation.appointment.infraestructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.diaryOpeningHours.SharedDiaryOpeningHoursPort;
import lombok.AllArgsConstructor;

import net.pladema.medicalconsultation.diary.repository.DiaryOpeningHoursRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class DiaryOpeningHoursPortImpl implements SharedDiaryOpeningHoursPort {

	private DiaryOpeningHoursRepository diaryOpeningHoursRepository;

	@Override
	public Boolean fetchIfOpeningHoursAllowWebAppointments(Integer diaryId, Integer openingHoursId) {
		return diaryOpeningHoursRepository.getIfExternalAppointmentsAreAllowed(diaryId, openingHoursId);
	}

	@Override
	public boolean fetchIfAppointmentCanBeAssignedAsOverturn(Integer diaryId, Integer openingHoursId, LocalDate appointmentDate) {
		return diaryOpeningHoursRepository.allowNewOverturn(diaryId, openingHoursId, appointmentDate);
	}

}
