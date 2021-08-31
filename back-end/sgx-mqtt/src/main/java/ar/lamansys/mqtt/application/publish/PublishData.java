package ar.lamansys.mqtt.application.publish;

import ar.lamansys.mqtt.application.MqttPublisher;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PublishData {

    private final MqttPublisher mqttPublisher;
    private final Logger logger;

    public PublishData(MqttPublisher mqttPublisher) {

        this.mqttPublisher = mqttPublisher;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public boolean run(MqttMetadataBo metadata) {
        logger.debug("Publish data {}", metadata);
        return mqttPublisher.run(metadata);
    }
}
