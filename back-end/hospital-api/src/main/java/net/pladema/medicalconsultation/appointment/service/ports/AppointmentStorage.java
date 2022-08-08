package net.pladema.medicalconsultation.appointment.service.ports;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentFilterBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentInfoBo;

public interface AppointmentStorage {
	List<AppointmentInfoBo> fetchAppointments(AppointmentFilterBo appointmentFilterBo);

	Collection<AppointmentBo> getAppointmentsByProfessionalInInstitution(Integer healthcareProfessionalId, Integer institutionId, LocalDate from, LocalDate to);

	Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds, LocalDate from, LocalDate to);

}
