package net.pladema.permissions.repository.enums;

import net.pladema.sgx.exceptions.NotFoundException;

public enum ERole {

	ROOT(1, "ROOT", true),
    ADMINISTRADOR(2, "ADMINISTRADOR", true),
    ESPECIALISTA_MEDICO(3, "ESPECIALISTA_MEDICO", false),
    PROFESIONAL_DE_SALUD(4, "PROFESIONAL_DE_SALUD", false),
    ADMINISTRATIVO(5, "ADMINISTRATIVO", false);

	private Short id;
    private String value;
    private Boolean isAdmin;
 
    ERole(Number id, String value, Boolean isAdmin) {
        this.id = id.shortValue();
        this.value = value;
        this.isAdmin = isAdmin;
    }
 
    public String getValue() {
        return value;
    }
    public Short getId() {
        return id;
    }
    public Boolean getIsAdmin() {
		return isAdmin;
	}

	public static ERole map(java.lang.Short id) {
        for(ERole e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new NotFoundException("role-not-exists", String.format("El rol %s no existe", id));
    }
}
