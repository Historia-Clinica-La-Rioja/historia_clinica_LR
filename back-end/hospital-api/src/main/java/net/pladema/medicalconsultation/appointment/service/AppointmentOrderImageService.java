package net.pladema.medicalconsultation.appointment.service;


import net.pladema.medicalconsultation.appointment.service.domain.AppointmentOrderImageBo;
import net.pladema.medicalconsultation.appointment.service.domain.DetailsOrderImageBo;

import java.util.Optional;


public interface AppointmentOrderImageService {


	boolean isAlreadyCompleted(Integer appointmentId);

	void updateCompleted(DetailsOrderImageBo detailsOrderImageBo);

	Optional<String> getImageId(Integer appointmentId);

	void save(AppointmentOrderImageBo appointmentOrderImageBo);

	void setImageId(Integer appointmentId, String imageId);

	void setDestInstitutionId(Integer institutionId, Integer appointmentId);

	void setReportStatusId(Integer appointmentId, Short reportStatusId);

    boolean updateOrderId(Integer appointmentId, Integer orderId, Boolean transcribed, Integer studyId);
}
