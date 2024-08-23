package net.pladema.imagenetwork.domain.exception;

import lombok.Getter;

@Getter
public enum EStudyException {
    VIEWER_URL_NOT_DEFINED,
    PAC_SERVER_NOT_FOUND,
    TOKEN_INVALID,
    ANY_FILEUUID_WAS_FOUND
}
