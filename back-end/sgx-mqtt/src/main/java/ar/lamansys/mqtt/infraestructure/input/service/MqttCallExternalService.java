package ar.lamansys.mqtt.infraestructure.input.service;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import ar.lamansys.mqtt.application.publish.PublishData;
import ar.lamansys.mqtt.application.removesubscription.RemoveSubscriptionFromUser;
import ar.lamansys.mqtt.application.subscribe.SubscribeData;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.mqtt.domain.SubscriptionBo;
import ar.lamansys.mqtt.infraestructure.input.rest.dto.MqttMetadataDto;

@Service
public class MqttCallExternalService {

	private final PublishData publishData;
	private final SubscribeData subscribeData;
	private final RemoveSubscriptionFromUser removeSubscriptionFromUser;

	public MqttCallExternalService(PublishData publishData,
								   SubscribeData subscribeData,
								   RemoveSubscriptionFromUser removeSubscriptionFromUser) {
		this.publishData = publishData;
		this.subscribeData = subscribeData;
		this.removeSubscriptionFromUser = removeSubscriptionFromUser;
	}
	public boolean publish(MqttMetadataDto mqttMetadataDto) {
		return publishData.run(mapTo(mqttMetadataDto));
	}

	public void subscribe(Integer userId, String topic, List<Consumer<MqttMetadataBo>> consumers) throws MqttException {
		subscribeData.run(new SubscriptionBo(userId, topic, consumers));
	}

	public void removeSubscriptionByUserId(Integer userId) {
		removeSubscriptionFromUser.run(userId);
	}

	private MqttMetadataBo mapTo(MqttMetadataDto metadata) {
		return new MqttMetadataBo(
				metadata.getTopic(),
				metadata.getMessage(),
				metadata.isRetained(),
				metadata.getQos()
		);
	}
}
