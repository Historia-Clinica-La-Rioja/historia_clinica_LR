package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentOrderDetailImageBO;

public interface AppointmentDetailOrderImageRepository {

	public AppointmentOrderDetailImageBO getOrderDetailImage(Integer appointmentId);

	public AppointmentOrderDetailImageBO getOrderTranscribedDetailImage(Integer appointmentId);
}
