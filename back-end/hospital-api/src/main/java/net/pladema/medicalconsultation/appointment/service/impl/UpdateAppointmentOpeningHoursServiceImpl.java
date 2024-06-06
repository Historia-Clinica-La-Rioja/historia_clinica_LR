package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;

import net.pladema.medicalconsultation.appointment.repository.EquipmentAppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.repository.AppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.service.UpdateAppointmentOpeningHoursService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateAppointmentOpeningHoursServiceImpl implements UpdateAppointmentOpeningHoursService {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateAppointmentOpeningHoursServiceImpl.class);

	private final AppointmentAssnRepository appointmentAssnRepository;

	private final AppointmentRepository appointmentRepository;

	private final EquipmentAppointmentAssnRepository equipmentAppointmentAssnRepository;

	@Override
	public AppointmentBo execute(AppointmentBo appointmentBo, boolean isAppointmentInEquipmentDiary) {
		LOG.debug("Input parameters -> appointmentBo {}", appointmentBo);

		if (isAppointmentInEquipmentDiary)
			equipmentAppointmentAssnRepository.updateOpeningHoursId(appointmentBo.getOpeningHoursId(), appointmentBo.getId());
		else
			appointmentAssnRepository.updateOpeningHoursId(appointmentBo.getOpeningHoursId(), appointmentBo.getId());

		if(appointmentBo.getAppointmentStateId().equals(AppointmentState.OUT_OF_DIARY)) {
			if(appointmentBo.getPatientId() != null)
				appointmentRepository.updateState(appointmentBo.getId(), AppointmentState.ASSIGNED, UserInfo.getCurrentAuditor(), LocalDateTime.now());
			else
				appointmentRepository.updateState(appointmentBo.getId(), AppointmentState.BOOKED, UserInfo.getCurrentAuditor(), LocalDateTime.now());
		}

		LOG.debug("Output -> {}", appointmentBo);
		return appointmentBo;
	}
}
