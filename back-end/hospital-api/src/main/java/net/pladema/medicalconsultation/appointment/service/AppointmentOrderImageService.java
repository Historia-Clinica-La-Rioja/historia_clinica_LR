package net.pladema.medicalconsultation.appointment.service;


import net.pladema.medicalconsultation.appointment.service.domain.AppointmentOrderImageBo;

import java.util.Optional;


public interface AppointmentOrderImageService {


	void updateCompleted(Integer appointmentId, boolean completed);

	Optional<String> getImageId(Integer appointmentId);

	void save(AppointmentOrderImageBo appointmentOrderImageBo);




}
