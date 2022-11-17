package ar.lamansys.mqtt.application.subscribe;


import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import ar.lamansys.mqtt.application.ports.MqttClientService;
import ar.lamansys.mqtt.domain.SubscriptionBo;



@Service
public class SubscribeData {

    private final MqttClientService mqttClient;

    public SubscribeData(MqttClientService mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void run(SubscriptionBo subscription) throws MqttException {
        mqttClient.subscribe(subscription, 2);
    }

}
