package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.diaryOpeningHours;

import java.time.LocalDate;

public interface SharedDiaryOpeningHoursPort {

	Boolean fetchIfOpeningHoursAllowWebAppointments(Integer diaryId, Integer openingHoursId);

	boolean fetchIfAppointmentCanBeAssignedAsOverturn(Integer diaryId, Integer openingHoursId, LocalDate appointmentDate);

}
