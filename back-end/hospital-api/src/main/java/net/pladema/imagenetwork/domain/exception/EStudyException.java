package net.pladema.imagenetwork.domain.exception;

import lombok.Getter;

@Getter
public enum EStudyException {
    VIEWER_URL_NOT_DEFINED,
    PAC_SERVER_NOT_FOUND,
    TOKEN_INVALID,
    ANY_FILEUUID_WAS_FOUND,
    STUDY_ALREADY_FINISHED,
    APPOINTMENT_NOT_FOUND, DIARY_NOT_FOUND, EQUIPMENT_NOT_FOUND, MODALITY_NOT_FOUND, PATIENT_NOT_FOUND,
}
