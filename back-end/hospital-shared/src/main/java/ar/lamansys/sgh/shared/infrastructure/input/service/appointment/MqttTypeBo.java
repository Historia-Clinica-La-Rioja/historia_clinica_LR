package ar.lamansys.sgh.shared.infrastructure.input.service.appointment;

import ar.lamansys.mqtt.domain.exception.MqttTypeBoException;
import ar.lamansys.mqtt.domain.exception.MqttTypeBoExceptionEnum;
import lombok.Getter;

@Getter
public enum MqttTypeBo {
    ADD("add");

    private String id;

    MqttTypeBo(String id) {
        this.id = id;
    }

    public static MqttTypeBo map(String id) {
        for(MqttTypeBo e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new MqttTypeBoException(MqttTypeBoExceptionEnum.INVALID_ID,String.format("El tipo %s no existe", id));
    }
}
