package ar.lamansys.mqtt.domain.exception;

public class MqttTypeBoException extends RuntimeException {

    public final MqttTypeBoExceptionEnum code;

    public MqttTypeBoException(MqttTypeBoExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}
