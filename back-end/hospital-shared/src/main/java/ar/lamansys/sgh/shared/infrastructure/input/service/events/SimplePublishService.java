package ar.lamansys.sgh.shared.infrastructure.input.service.events;

import ar.lamansys.mqtt.infraestructure.input.MqttDtoUtils;
import ar.lamansys.mqtt.infraestructure.input.rest.dto.MqttMetadataDto;
import ar.lamansys.mqtt.infraestructure.input.service.MqttCallExternalService;

public class SimplePublishService {

	private final MqttCallExternalService mqttCallExternalService;
	private final String namePrefix;

	public SimplePublishService(MqttCallExternalService mqttCallExternalService, String namePrefix) {
		this.mqttCallExternalService = mqttCallExternalService;
		this.namePrefix = namePrefix;
	}

	public void publish(Integer patientId, String topic) {
		String fullTopic = namePrefix + "/" + topic;
		String message = getSimplePayload(patientId, fullTopic);
		MqttMetadataDto mqttMetadataDto = MqttDtoUtils.getMqtMetadataDto(fullTopic, message);
		mqttCallExternalService.publish(mqttMetadataDto);
	}

	private String getSimplePayload(Integer patientId, String topic) {
		return String.format("\"description\":\"{\\\"patientId\\\":%d,\\\"topic\\\":\\\"%s\\\"}\"", patientId, topic);
	}


}
