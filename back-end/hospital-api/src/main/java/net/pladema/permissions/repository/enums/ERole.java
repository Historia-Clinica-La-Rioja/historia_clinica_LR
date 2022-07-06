package net.pladema.permissions.repository.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum ERole {

	ROOT(1, "ROOT", ERoleLevel.LEVEL0),
    ADMINISTRADOR(2, "ADMINISTRADOR", ERoleLevel.LEVEL0),
    ESPECIALISTA_MEDICO(3, "ESPECIALISTA_MEDICO", ERoleLevel.LEVEL1),
    PROFESIONAL_DE_SALUD(4, "PROFESIONAL_DE_SALUD", ERoleLevel.LEVEL1),
    ADMINISTRATIVO(5, "ADMINISTRATIVO", ERoleLevel.LEVEL1),
    ENFERMERO_ADULTO_MAYOR(6, "ENFERMERO_ADULTO_MAYOR", ERoleLevel.LEVEL1),
    ENFERMERO(7,"ENFERMERO",ERoleLevel.LEVEL1),
	ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE(8,"ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE", ERoleLevel.LEVEL1),
    ADMINISTRADOR_AGENDA(9,"ADMINISTRADOR_AGENDA", ERoleLevel.LEVEL1),
    API_CONSUMER(10, "API_CONSUMER", ERoleLevel.LEVEL0),
    ESPECIALISTA_EN_ODONTOLOGIA(11, "ESPECIALISTA_EN_ODONTOLOGIA", ERoleLevel.LEVEL1),
    ADMINISTRADOR_DE_CAMAS(12, "ADMINISTRADOR_DE_CAMAS", ERoleLevel.LEVEL1),
	PERSONAL_DE_IMAGENES(13, "PERSONAL_DE_IMAGENES", ERoleLevel.LEVEL1),
	PERSONAL_DE_LABORATORIO(14, "PERSONAL_DE_LABORATORIO", ERoleLevel.LEVEL1),
	PERSONAL_DE_FARMACIA(15, "PERSONAL_DE_FARMACIA", ERoleLevel.LEVEL1),
	PERSONAL_DE_ESTADISTICA(16, "PERSONAL_DE_ESTADISTICA", ERoleLevel.LEVEL1),
	PARTIALLY_AUTHENTICATED(17, "PARTIALLY_AUTHENTICATED", ERoleLevel.LEVEL0),
	;

    private Short id;
    private String value;
    private ERoleLevel level;
 
    ERole(Number id, String value, ERoleLevel level) {
        this.id = id.shortValue();
        this.value = value;
        this.level = level;
    }
 
    public String getValue() {
        return value;
    }
    public Short getId() {
        return id;
    }
    public ERoleLevel getLevel() {
		return level;
	}

	public static ERole map(java.lang.Short id) {
        for(ERole e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new NotFoundException("role-not-exists", String.format("El rol %s no existe", id));
    }
}
