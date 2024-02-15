package net.pladema.imagenetwork.application.exception;

import lombok.Getter;

@Getter
public enum StudyExceptionEnum {
    VIEWER_URL_NOT_DEFINED,
    PAC_SERVER_NOT_FOUND,
    TOKEN_INVALID,
    ANY_FILEUUID_WAS_FOUND
}
