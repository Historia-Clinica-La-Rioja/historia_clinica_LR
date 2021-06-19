package ar.lamansys.sgh.clinichistory.application.calculatecie10.exceptions;

import lombok.Getter;

@Getter
public class HCICIE10Exception extends RuntimeException {

    private HCICIE10ExceptionEnum code;

    public HCICIE10Exception(HCICIE10ExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}