package ar.lamansys.mqtt.application.publish;

import org.springframework.stereotype.Service;

import ar.lamansys.mqtt.application.ports.MqttClientService;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PublishData {

	private final MqttClientService mqttClient;

	public PublishData(MqttClientService mqttClient) {
		this.mqttClient = mqttClient;
	}

	public boolean run(MqttMetadataBo message) {
		return mqttClient.publish(message);
	}
}
