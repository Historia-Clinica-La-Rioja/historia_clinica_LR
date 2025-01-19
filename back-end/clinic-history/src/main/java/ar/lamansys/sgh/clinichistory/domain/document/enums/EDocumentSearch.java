package ar.lamansys.sgh.clinichistory.domain.document.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum EDocumentSearch {

    DIAGNOSIS("diagnosis"),
    DOCTOR("doctor"),
    CREATED_ON("createdOn"),
	DOCUMENT_TYPE("documentType"),
    ;

    private String value;
}
