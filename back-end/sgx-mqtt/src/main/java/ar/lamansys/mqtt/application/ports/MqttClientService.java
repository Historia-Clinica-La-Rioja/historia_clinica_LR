package ar.lamansys.mqtt.application.ports;

import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.mqtt.domain.SubscriptionBo;

public interface MqttClientService {

	boolean publish(MqttMetadataBo mqttMetadataBo);

	boolean subscribe(SubscriptionBo subscriptionBo, int qos);
	boolean removeSubscriptionByUserId(Integer userId);
}
