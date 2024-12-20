package net.pladema.medicalconsultation.appointment.service;

import ar.lamansys.mqtt.domain.MqttMetadataBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import java.util.Optional;

public interface EquipmentAppointmentService {

    Optional<AppointmentBo> getEquipmentAppointment(Integer appointmentId);

    MqttMetadataBo setToPublishWorkList(Integer institutionId, Integer appointmentId);

}
