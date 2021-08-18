package ar.lamansys.sgx.shared.scheduling.infrastructure.output.service;

public enum ESyncError {

    IMMUNIZATION("Immunization"),
    ;
	
    private String value;

    ESyncError(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
