package net.pladema.medicalconsultation.equipmentdiary.application.port.output;

import net.pladema.medicalconsultation.equipmentdiary.domain.UpdateEquipmentDiaryAppointmentBo;

import java.util.List;

public interface EquipmentDiaryPort {

	List<UpdateEquipmentDiaryAppointmentBo> getUpdateEquipmentDiaryAppointments(Integer equipmentDiaryId);

}
