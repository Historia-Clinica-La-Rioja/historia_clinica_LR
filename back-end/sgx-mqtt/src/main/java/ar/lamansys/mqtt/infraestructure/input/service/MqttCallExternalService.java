package ar.lamansys.mqtt.infraestructure.input.service;

import ar.lamansys.mqtt.application.publish.PublishData;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.mqtt.infraestructure.input.rest.dto.MqttMetadataDto;
import org.springframework.stereotype.Service;

@Service
public class MqttCallExternalService {

    private final PublishData publishData;

    public MqttCallExternalService(PublishData publishData) {
        this.publishData = publishData;
    }

    public boolean publish(MqttMetadataDto mqttMetadataDto) {
        return this.publishData.run(mapTo(mqttMetadataDto));
    }

    private MqttMetadataBo mapTo(MqttMetadataDto metadata) {
        return new MqttMetadataBo(
                metadata.getTopic(),
                metadata.getMessage(),
                metadata.isRetained(),
                metadata.getQos(),
                metadata.getType()
        );
    }
}
