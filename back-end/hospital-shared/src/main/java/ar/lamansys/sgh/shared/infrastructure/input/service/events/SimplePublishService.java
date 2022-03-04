package ar.lamansys.sgh.shared.infrastructure.input.service.events;

import ar.lamansys.mqtt.infraestructure.input.MqttDtoUtils;
import ar.lamansys.mqtt.infraestructure.input.rest.dto.MqttMetadataDto;
import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.EventTopicDto;

import org.springframework.stereotype.Service;

@Service
public class SimplePublishService {

	private final MqttCallExternalService mqttCallExternalService;

	public SimplePublishService(MqttCallExternalService mqttCallExternalService) {
		this.mqttCallExternalService = mqttCallExternalService;
	}

	public void publish(Integer patientId, EventTopicDto eventTopicDto) {
		String message = getSimplePayload(patientId, eventTopicDto.getId());
		MqttMetadataDto mqttMetadataDto = MqttDtoUtils.getMqtMetadataDto(eventTopicDto.getDescription(), message);
		mqttCallExternalService.publish(mqttMetadataDto);
	}

	private String getSimplePayload(Integer patientId, Integer eventId) {
		return String.format("\"description\":\"{\\\"patientId\\\":%d,\\\"eventId\\\":%d}\"", patientId, eventId);
	}


}
