package ar.lamansys.mqtt.domain.exception;

public class SubscriptionBoException extends RuntimeException {

    public final SubscriptionBoExceptionEnum code;

    public SubscriptionBoException(SubscriptionBoExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}
