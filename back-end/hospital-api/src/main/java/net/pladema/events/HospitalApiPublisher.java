package net.pladema.events;

import ar.lamansys.sgh.shared.infrastructure.input.service.events.EmergencyCareEpisodeNotificationDto;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.events.NotifyPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.PublisherFactory;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.SimplePublishService;

@Service
public class HospitalApiPublisher {

	private final SimplePublishService simplePublishService;

	public HospitalApiPublisher(PublisherFactory publisherFactory) {
		simplePublishService = publisherFactory.getPublisher("HOSPITAL_API");
	}

	public void publish(Integer patientId, Integer institutionId, EHospitalApiTopicDto eHospitalApiTopicDto) {
		simplePublishService.publish(patientId, institutionId, eHospitalApiTopicDto.toString());
	}

	public void appointmentCaller(NotifyPatientDto notifyPatientDto, Integer institutionId) {
		simplePublishService.appointmentCallerPublish(EHospitalApiTopicDto.PACIENTE_LLAMADO.toString(), notifyPatientDto, institutionId);
	}

	public void emergencyCareAppointmentCaller(EmergencyCareEpisodeNotificationDto notification, Integer institutionId) {
		simplePublishService.emergencyCareCallerPublish(EHospitalApiTopicDto.EMERGENCY_CARE_SCHEDULER_CALL.toString(), notification, institutionId);
	}

}
