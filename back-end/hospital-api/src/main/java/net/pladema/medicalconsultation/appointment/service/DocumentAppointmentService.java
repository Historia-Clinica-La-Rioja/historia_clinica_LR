package net.pladema.medicalconsultation.appointment.service;

import java.util.Optional;

import net.pladema.medicalconsultation.appointment.service.domain.DocumentAppointmentBo;

public interface DocumentAppointmentService {

	void save(DocumentAppointmentBo documentAppointmentBo);

	void delete(DocumentAppointmentBo documentAppointmentBo);

	Optional<DocumentAppointmentBo> getDocumentAppointment(Integer appointmentId);
}
