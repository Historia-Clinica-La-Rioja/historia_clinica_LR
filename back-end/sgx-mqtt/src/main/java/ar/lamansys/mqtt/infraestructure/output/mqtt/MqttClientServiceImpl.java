package ar.lamansys.mqtt.infraestructure.output.mqtt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import ar.lamansys.mqtt.application.ports.MqttClientService;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.mqtt.domain.SubscriptionBo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttClientServiceImpl implements MqttClientService {
    private final IMqttClient mqttClient;
	private final SgxMqttCallback callback;

	private List<String> topicRegistered;

    public MqttClientServiceImpl(String mqttServerAddress, String mqttPublisherId, String username, String password) {
        this.mqttClient = connect(mqttServerAddress, mqttPublisherId, username, password);
		this.callback = new SgxMqttCallback();
		this.mqttClient.setCallback(callback);
		this.topicRegistered = new ArrayList<>();
    }

    @Override
    public boolean publish(MqttMetadataBo mqttMetadataBo) {
		try {
			MqttMessage mqttMessage = new MqttMessage(mqttMetadataBo.getMessageBytes());
			mqttMessage.setQos(mqttMetadataBo.getQos());
			mqttMessage.setRetained(mqttMetadataBo.isRetained());
			mqttClient.publish(mqttMetadataBo.getTopic(), mqttMessage);
		} catch (MqttException exc) {
			return false;
		}
		return true;
    }

    @Override
    public boolean subscribe(SubscriptionBo subscriptionBo, int qos) {
		try {
			mqttClient.subscribe(subscriptionBo.getTopic(), qos);
			callback.addAction(subscriptionBo);
			topicRegistered.add(subscriptionBo.getTopic());
		} catch (MqttException exc) {
			return false;
		}
		return true;
    }

	@Override
	public boolean removeSubscriptionByUserId(Integer userId) {
		callback.removeSubscriptionByUserId(userId);
		topicRegistered.stream()
				.filter(topic -> !callback.containSubscriptions(topic))
				.forEach(topic -> {
					try {
						mqttClient.unsubscribe(topic);
					} catch (MqttException e) {
						throw new RuntimeException(e);
					}
				});
		topicRegistered = topicRegistered.stream()
				.filter(callback::containSubscriptions)
				.collect(Collectors.toList());
		return true;
	}

	public IMqttClient connect(String mqttServerAddress, String mqttPublisherId, String username, String password) {
        try {
			MqttClient client = new MqttClient(mqttServerAddress, mqttPublisherId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
			options.setUserName(username);
			options.setPassword(password != null ? password.toCharArray() : null);
            client.connect(options);
            return client;
        } catch (MqttException e) {
            log.error("Can't connect to {} as {}: {}", mqttServerAddress, mqttPublisherId, e.getMessage(), e);
        }
        return null;

    }
}
