package net.pladema.user.application.getuserpersoninfo.exception;

public class GetUserPersonInfoException extends RuntimeException {

    private final GetUserPersonInfoEnumException code;

    public GetUserPersonInfoException(GetUserPersonInfoEnumException code, String message) {
        super(message);
        this.code = code;
    }
}
