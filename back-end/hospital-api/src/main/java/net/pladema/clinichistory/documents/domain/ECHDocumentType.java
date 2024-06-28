package net.pladema.clinichistory.documents.domain;

import lombok.Getter;

@Getter
public enum ECHDocumentType {

    EPICRISIS("Epicrisis"),
    REPORTS("Informes"),
    MEDICAL_PRESCRIPTIONS("Prescripciones Médicas"),
    CLINICAL_NOTES("Notas Clínicas"),
    ANESTHETIC_REPORTS("Partes anestésicos"),
    OTHER("Otro"),
    NOT_SUPPORTED("No soportado");

    private final String value;

    ECHDocumentType(String value) {
		this.value = value;
	}

}
