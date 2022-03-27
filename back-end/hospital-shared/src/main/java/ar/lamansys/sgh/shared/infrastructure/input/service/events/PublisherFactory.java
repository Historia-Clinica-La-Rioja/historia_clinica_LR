package ar.lamansys.sgh.shared.infrastructure.input.service.events;

import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;

import ar.lamansys.sgh.shared.infrastructure.input.service.events.exceptions.NewPublisherException;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublisherFactory {

	private final List<String> assignedNames = new ArrayList<>();
	private final MqttCallExternalService mqttCallExternalService;

	public PublisherFactory(MqttCallExternalService mqttCallExternalService) {
		this.mqttCallExternalService = mqttCallExternalService;
	}

	public SimplePublishService getPublisher(String namePrefix) {
		if (assignedNames.stream().anyMatch(n -> n.equals(namePrefix))) {
			throw new NewPublisherException(namePrefix);
		}
		assignedNames.add(namePrefix);
		return new SimplePublishService(mqttCallExternalService, namePrefix);
	}

}
