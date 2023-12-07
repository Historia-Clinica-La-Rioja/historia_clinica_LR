package ar.lamansys.mqtt.domain;

import java.util.Objects;

import lombok.*;

@Getter
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("MqttMetadataBo{");
		sb.append("topic='").append(topic).append('\'');
		sb.append(", message='").append(message).append('\'');
		sb.append(", retained=").append(retained);
		sb.append(", qos=").append(qos);
		sb.append('}');
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MqttMetadataBo that = (MqttMetadataBo) o;
		return retained == that.retained && Objects.equals(topic, that.topic) && Objects.equals(message, that.message) && Objects.equals(qos, that.qos);
	}

	@Override
	public int hashCode() {
		return Objects.hash(topic, message, retained, qos);
	}
}