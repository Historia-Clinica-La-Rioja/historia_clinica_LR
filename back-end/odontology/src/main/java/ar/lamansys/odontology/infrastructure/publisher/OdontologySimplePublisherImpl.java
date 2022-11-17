package ar.lamansys.odontology.infrastructure.publisher;

import org.springframework.stereotype.Service;

import ar.lamansys.odontology.domain.EOdontologyTopicDto;
import ar.lamansys.odontology.domain.Publisher;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.PublisherFactory;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.SimplePublishService;

@Service
public class OdontologySimplePublisherImpl implements Publisher {

	private SimplePublishService simplePublishService;

	public OdontologySimplePublisherImpl(PublisherFactory publisherFactory) {
		simplePublishService = publisherFactory.getPublisher("ODONTOLOGY");
	}

	@Override
	public void run(Integer patientId, Integer institutionId, EOdontologyTopicDto EOdontologyTopicDto) {
		simplePublishService.publish(patientId, institutionId, EOdontologyTopicDto.toString());
	}

	// Podria agregarse un metodo que acepte topico global compartido en shared
	// y que no publique el modulo que lo genera ( Odonto en este caso ).
}


