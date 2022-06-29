package ar.lamansys.mqtt.domain;

import lombok.*;

@Getter
@EqualsAndHashCode()
@ToString
public class MqttMetadataBo {

    private final String topic;

    private final String message;

    private final boolean retained;

    private final Integer qos;


    public MqttMetadataBo(String topic, String message, boolean retained, Integer qos) {
        this.message = message;
        this.retained = retained;
        this.topic = topic;
        this.qos = qos;
    }

    public byte[] getMessageBytes() {
        return message.getBytes();
    }

	public String getMessage() {
		return message;
	}

}