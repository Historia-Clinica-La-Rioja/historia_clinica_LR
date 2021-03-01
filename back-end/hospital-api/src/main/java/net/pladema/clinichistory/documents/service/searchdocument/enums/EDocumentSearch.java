package net.pladema.clinichistory.documents.service.searchdocument.enums;

public enum EDocumentSearch {

    DIAGNOSIS("diagnosis"),
    DOCTOR("doctor"),
    CREATED_ON("createdOn");

    private String value;

    EDocumentSearch(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
