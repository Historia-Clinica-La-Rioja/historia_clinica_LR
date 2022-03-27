package net.pladema.events;

import ar.lamansys.sgh.shared.infrastructure.input.service.events.PublisherFactory;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.SimplePublishService;

import org.springframework.stereotype.Service;

@Service
public class HospitalApiPublisher {

	private SimplePublishService simplePublishService;

	public HospitalApiPublisher(PublisherFactory publisherFactory) {
		simplePublishService = publisherFactory.getPublisher("hospitalApi");
	}

	public void publish(Integer patientId, EHospitalApiTopicDto eHospitalApiTopicDto) {
		simplePublishService.publish(patientId, eHospitalApiTopicDto.toString());
	}
}
