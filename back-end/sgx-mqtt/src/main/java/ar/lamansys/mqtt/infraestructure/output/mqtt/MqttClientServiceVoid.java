package ar.lamansys.mqtt.infraestructure.output.mqtt;

import ar.lamansys.mqtt.application.ports.MqttClientService;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.mqtt.domain.SubscriptionBo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttClientServiceVoid implements MqttClientService {

    @Override
	public boolean publish(MqttMetadataBo mqttMetadataBo) {
        log.warn("MQTT disabled, won't publish {}", mqttMetadataBo.getTopic());
		return false;
    }

	@Override
	public boolean subscribe(SubscriptionBo subscriptionBo, int qos) {
		log.warn("MQTT disabled, won't subscribe to {}", subscriptionBo.getTopic());
		return false;
	}
	@Override
	public boolean removeSubscriptionByUserId(Integer userId) {
		log.warn("MQTT removeSubscriptionByUserId, won't remove Subscription from UserId to {}", userId);
		return false;
	}


}
