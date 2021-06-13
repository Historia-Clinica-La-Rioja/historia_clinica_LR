package ar.lamansys.odontology.application.odontogram.exception;

public class ToothNotFoundException extends RuntimeException {

    private final ToothExceptionEnum code;
    public ToothNotFoundException(ToothExceptionEnum code, String errorMessage) {
        super(errorMessage);
        this.code = code;
    }

    public String getCode() {
        return this.code.name();
    }
}
