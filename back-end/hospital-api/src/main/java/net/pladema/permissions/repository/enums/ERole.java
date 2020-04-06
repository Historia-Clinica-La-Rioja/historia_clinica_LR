package net.pladema.permissions.repository.enums;

public enum ERole {

	ADMIN("ADMIN"),
    ADMINAPP("ADMIN_APP"),
    BACKOFFICE_USER("BACKOFFICE_USER"),
    PATIENT_USER("PATIENT_USER");
 
    private String value;
 
    ERole(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
