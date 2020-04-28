package net.pladema.permissions.repository.enums;

import net.pladema.sgx.exceptions.NotFoundException;

public enum ERole {

	ROOT(1, "ROOT"),
    ADMINISTRADOR(2, "ADMINISTRADOR"),
    ESPECIALISTA_MEDICO(3, "ESPECIALISTA_MEDICO"),
    PROFESIONAL_DE_SALUD(4, "PROFESIONAL_DE_SALUD"),
    ADMINISTRATIVO(5, "ADMINISTRATIVO");

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
