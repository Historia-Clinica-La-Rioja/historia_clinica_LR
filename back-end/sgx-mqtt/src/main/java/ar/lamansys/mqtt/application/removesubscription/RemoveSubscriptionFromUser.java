package ar.lamansys.mqtt.application.removesubscription;

import org.springframework.stereotype.Service;

import ar.lamansys.mqtt.application.ports.MqttClientService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RemoveSubscriptionFromUser {

	private final MqttClientService mqttClientService;

	public RemoveSubscriptionFromUser(MqttClientService mqttClientService) {
		this.mqttClientService = mqttClientService;
	}

	public void run(Integer userId) {
		log.debug("RemoveSubscriptionFromUser userId {}", userId);
		mqttClientService.removeSubscriptionByUserId(userId);
	}
}
