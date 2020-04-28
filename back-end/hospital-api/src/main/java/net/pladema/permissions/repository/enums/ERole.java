package net.pladema.permissions.repository.enums;

import net.pladema.sgx.exceptions.NotFoundException;

public enum ERole {

	ADMIN(1, "ADMIN"),
    ADMIN_APP(2, "ADMIN_APP"),
    BASIC_USER(3, "BASIC_USER"),
    ADVANCED_USER(4, "ADVANCED_USER");

	private Short id;
    private String value;
 
    ERole(Number id, String value) {
        this.id = id.shortValue();
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
    public Short getId() {
        return id;
    }


    public static ERole map(java.lang.Short id) {
        for(ERole e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new NotFoundException("role-not-exists", String.format("El rol %s no existe", id));
    }
}
