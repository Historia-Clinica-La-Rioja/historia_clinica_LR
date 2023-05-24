package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

public interface CreateTranscribedEquipmentAppointmentService {

    AppointmentBo execute(AppointmentBo appointment, Integer orderId);
}
