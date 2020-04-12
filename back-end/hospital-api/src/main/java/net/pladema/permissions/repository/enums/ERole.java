package net.pladema.permissions.repository.enums;

public enum ERole {

	ADMIN("ADMIN"),
    ADMIN_APP("ADMIN_APP"),
    ADVANCED_USER("ADVANCED_USER"),
    BASIC_USER("BASIC_USER");
 
    private String value;
 
    ERole(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
