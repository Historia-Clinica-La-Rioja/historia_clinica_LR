package ar.lamansys.mqtt.application;

import ar.lamansys.mqtt.domain.MqttMetadataBo;

public interface MqttPublisher {
    boolean run(MqttMetadataBo mqttMetadataBo);
}
