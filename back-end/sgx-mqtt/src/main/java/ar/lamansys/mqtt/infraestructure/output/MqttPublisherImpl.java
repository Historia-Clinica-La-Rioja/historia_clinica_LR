package ar.lamansys.mqtt.infraestructure.output;

import ar.lamansys.mqtt.application.MqttPublisher;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public class MqttPublisherImpl implements MqttPublisher {
    private final IMqttClient mqttClient;

    public MqttPublisherImpl(IMqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @Override
    public boolean run(MqttMetadataBo mqttMetadataBo) {
        try {
            MqttMessage mqttMessage = new MqttMessage(mqttMetadataBo.getMessage());
            mqttMessage.setQos(mqttMetadataBo.getQos());
            mqttMessage.setRetained(mqttMetadataBo.isRetained());
            this.mqttClient.publish(mqttMetadataBo.getTopic(), mqttMessage);
        } catch (MqttException exc) {
            return false;
        }
        return true;
    }
}
