package net.pladema.clinichistory.documents.events.exceptions;

import lombok.Getter;

@Getter
public enum GenerateDocumentEventEnumException {
    TOKEN_NOT_FOUND,
    DOCUMENT_NULL, DOCUMENT_ID_NULL, INSTITUTION_ID_NULL, ENCOUNTER_ID_NULL, PATIENT_ID_NULL;

}
