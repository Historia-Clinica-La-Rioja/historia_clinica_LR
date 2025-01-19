package net.pladema.medicalconsultation.equipmentdiary.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.equipmentdiary.application.port.output.EquipmentDiaryPort;
import net.pladema.medicalconsultation.equipmentdiary.domain.UpdateEquipmentDiaryAppointmentBo;

import net.pladema.medicalconsultation.equipmentdiary.repository.EquipmentDiaryRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EquipmentDiaryPortImpl implements EquipmentDiaryPort {

	private final EquipmentDiaryRepository equipmentDiaryRepository;

	@Override
	public List<UpdateEquipmentDiaryAppointmentBo> getUpdateEquipmentDiaryAppointments(Integer equipmentDiaryId) {
		Short APPOINTMENT_CANCELLED_STATE_ID = 4;
		return equipmentDiaryRepository.fetchUpdateEquipmentDiaryAppointments(equipmentDiaryId, APPOINTMENT_CANCELLED_STATE_ID);
	}

}
