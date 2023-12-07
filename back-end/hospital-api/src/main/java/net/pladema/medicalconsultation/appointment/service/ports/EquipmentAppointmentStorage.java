package net.pladema.medicalconsultation.appointment.service.ports;

import net.pladema.medicalconsultation.appointment.service.domain.EquipmentAppointmentBo;

import java.time.LocalDate;
import java.util.List;

public interface EquipmentAppointmentStorage {

    List<EquipmentAppointmentBo> getAppointmentsByEquipmentId(Integer equipmentId, Integer institutionId, LocalDate from, LocalDate to);

}
