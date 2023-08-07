package ar.lamansys.virtualConsultation.infrastructure.mqtt;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.events.PublisherFactory;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.SimplePublishService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VirtualConsultationPublisher {

	private final SimplePublishService simplePublishService;

	public VirtualConsultationPublisher(PublisherFactory publisherFactory) {
		simplePublishService = publisherFactory.getPublisher("VIRTUAL-CONSULTATION");
	}

	public void publish(String subTopic, String message) {
		log.debug("Input parameters -> subTopic {}, message {}", subTopic, message);
		simplePublishService.genericPublish(subTopic, message);
	}

}
