package ar.lamansys.mqtt.infraestructure.input;

import ar.lamansys.mqtt.infraestructure.input.rest.dto.MqttMetadataDto;

public class MqttDtoUtils {

	public static MqttMetadataDto getMqtMetadataDto(String topic, String message) {
		return new MqttMetadataDto(topic, message, false, 2);
	}

	private MqttDtoUtils(){}

}
