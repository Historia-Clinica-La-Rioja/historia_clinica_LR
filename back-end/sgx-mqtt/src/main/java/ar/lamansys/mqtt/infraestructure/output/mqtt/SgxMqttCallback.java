package ar.lamansys.mqtt.infraestructure.output.mqtt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.mqtt.domain.SubscriptionBo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SgxMqttCallback implements IMqttMessageListener, MqttCallbackExtended {

    private List<SubscriptionBo> subscriptions;

    public SgxMqttCallback() {
		this.subscriptions = new ArrayList<>();
    }

    @Override
    public void connectionLost(Throwable throwable) {
		throwable.printStackTrace();
	}

    @Override
    public void messageArrived(String topic, MqttMessage message) {
		subscriptions.stream()
				.filter(s -> s.apply(topic))
				.forEach(s -> s.consume(mapTo(topic, message)));
    }

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
	}

	@Override
	public void connectComplete(boolean connected, String s) {
	}

	private MqttMetadataBo mapTo(String topic, MqttMessage message) {
		return new MqttMetadataBo(topic, message.toString(), message.isRetained(), message.getQos());
	}


    public void addAction(SubscriptionBo subscription) {
		subscriptions.stream().filter(s-> s.equals(subscription))
			.findFirst()
			.ifPresentOrElse(
					s -> s.addActions(subscription.getActions()),
					() -> subscriptions.add(subscription)
			);
    }

	public void removeSubscriptionByUserId(Integer userId) {
		subscriptions
				.forEach((s) -> s.removeActionsFromUser(userId));
		subscriptions = subscriptions.stream()
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());
	}

	public boolean containSubscriptions(String topic) {
		if (topic == null)
			return false;
		return subscriptions.stream()
				.anyMatch(s -> s.apply(topic));

	}

}
