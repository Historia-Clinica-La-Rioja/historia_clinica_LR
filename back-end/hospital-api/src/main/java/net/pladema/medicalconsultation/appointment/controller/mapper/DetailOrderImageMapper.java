package net.pladema.medicalconsultation.appointment.controller.mapper;


import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentOrderDetailImageDto;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentOrderDetailImageBO;
import net.pladema.staff.controller.dto.ProfessionalDto;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DetailOrderImageMapper {

	@Named("parseToAppointmentOrderDetailDto")
	public AppointmentOrderDetailImageDto parseToAppointmentOrderDetailDto(AppointmentOrderDetailImageBO appointmentOrderDetailBO, ProfessionalDto professionalDto){
		log.trace("Input -> StudyOrderReportInfoBo {}", appointmentOrderDetailBO);
		AppointmentOrderDetailImageDto result = new AppointmentOrderDetailImageDto();

		result.setObservations(appointmentOrderDetailBO.getObservations());
		result.setProfessional(professionalDto);
		result.setCreationDate(appointmentOrderDetailBO.getCreationDate());
		result.setProfessionalOrderTranscribed(null);
		result.setHealthCondition(appointmentOrderDetailBO.getHealthCondition());
		result.setIdServiceRequest(appointmentOrderDetailBO.getIdServiceRequest());
		log.trace("Output: {}", result);
		return result;
	}

	@Named("parseToAppointmentOrderTranscribedDetailDto")
	public AppointmentOrderDetailImageDto parseToAppointmentOrderTranscribedDetailDto(AppointmentOrderDetailImageBO appointmentOrderDetailBO){
		log.trace("Input -> StudyOrderReportInfoBo {}", appointmentOrderDetailBO);
		AppointmentOrderDetailImageDto result = new AppointmentOrderDetailImageDto();

		result.setObservations(appointmentOrderDetailBO.getObservations());
		result.setProfessional(null);
		result.setCreationDate(appointmentOrderDetailBO.getCreationDate());
		result.setProfessionalOrderTranscribed(appointmentOrderDetailBO.getProfessionalOrderTranscribed());
		result.setHealthCondition(appointmentOrderDetailBO.getHealthCondition());
		result.setIdServiceRequest(appointmentOrderDetailBO.getIdServiceRequest());
		log.trace("Output: {}", result);
		return result;
	}

}
