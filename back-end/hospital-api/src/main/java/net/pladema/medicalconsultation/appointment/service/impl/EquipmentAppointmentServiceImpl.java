package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.*;
import net.pladema.medicalconsultation.appointment.service.EquipmentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EquipmentAppointmentServiceImpl implements EquipmentAppointmentService {

	private static final String OUTPUT = "Output -> {}";

	private final EquipmentAppointmentAssnRepository equipmentAppointmentAssnRepository;

	private final SharedReferenceCounterReference sharedReferenceCounterReference;

	public EquipmentAppointmentServiceImpl(EquipmentAppointmentAssnRepository equipmentAppointmentAssnRepository, SharedReferenceCounterReference sharedReferenceCounterReference) {
		this.equipmentAppointmentAssnRepository = equipmentAppointmentAssnRepository;
		this.sharedReferenceCounterReference = sharedReferenceCounterReference;
	}


	@Override
	public Optional<AppointmentBo> getEquipmentAppointment(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<AppointmentBo>	result = equipmentAppointmentAssnRepository.getEquipmentAppointment(appointmentId).stream().findFirst().map(AppointmentBo::fromAppointmentVo);
		if (result.isPresent()) {
			List<Integer> diaryIds = result.stream().map(AppointmentBo::getDiaryId).collect(Collectors.toList());
			result = setIsAppointmentProtected(result.stream().collect(Collectors.toList()), diaryIds)
					.stream().findFirst();
		}
		log.debug(OUTPUT, result);
		return result;
	}

	private Collection<AppointmentBo> setIsAppointmentProtected(Collection<AppointmentBo> appointments, List<Integer> diaryIds) {
		List<Integer> protectedAppointments = sharedReferenceCounterReference.getProtectedAppointmentsIds(diaryIds);
		appointments.stream().forEach(a -> {
			if (protectedAppointments.contains(a.getId()))
				a.setProtected(true);
			else
				a.setProtected(false);
		});
		return appointments;
	}

	@Override
	public boolean updateEquipmentState(Integer appointmentId, short appointmentStateId, Integer userId, String reason) {
		return false;
	}

	@Override
	public AppointmentBo updateEquipmentAppointment(UpdateAppointmentBo appointmentDto) {
		return null;
	}
}
