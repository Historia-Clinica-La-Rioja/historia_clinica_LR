package ar.lamansys.odontology.infrastructure.publisher;

import ar.lamansys.odontology.domain.Publisher;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.EventTopicDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.SimplePublishService;

import org.springframework.stereotype.Service;

@Service
public class PublisherImpl implements Publisher {

	private final SimplePublishService simplePublishService;

	public PublisherImpl(SimplePublishService simplePublishService) {
		this.simplePublishService = simplePublishService;
	}

	@Override
	public void run(Integer patientId, EventTopicDto eventTopicDto) {
		simplePublishService.publish(patientId, eventTopicDto);
	}
}
