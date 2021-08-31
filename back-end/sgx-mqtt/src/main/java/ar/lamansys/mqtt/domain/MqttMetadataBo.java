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

    private final MqttTypeBo type;

    public MqttMetadataBo(String topic, String message, boolean retained, Integer qos, String type) {
        this.message = message;
        this.retained = retained;
        this.topic = topic;
        this.qos = qos;
        this.type = MqttTypeBo.map(type);
    }

    public byte[] getMessage() {
        return String.format("{\"type\":\"%s\",%s}",type.getId(),message).getBytes();
        //String a = "{\"type\":\"add\",\"data\":{\"appointmentId\":1,\"patient\":\"Trapa\",\"sector\":1,\"doctor\":\"Nick Riviera\",\"doctorsOffice\":\"Consultorio 3\"}}";
    }

}