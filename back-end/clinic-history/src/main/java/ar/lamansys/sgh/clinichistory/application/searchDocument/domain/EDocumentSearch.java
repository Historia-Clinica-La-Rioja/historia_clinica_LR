package ar.lamansys.sgh.clinichistory.application.searchDocument.domain;

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
