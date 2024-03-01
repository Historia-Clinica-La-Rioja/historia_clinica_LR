package net.pladema.procedure.domain;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EProcedureTemplateStatusBo {
	DRAFT(1, "Borrador", true),
	ACTIVE(2, "Activa", false),
	INACTIVE(3, "Inactiva", false)
	//When adding new states please update the getNextStateMethod
	;
	private final Short id;
	private final String value;
	private final Boolean allowsUpdate;
	EProcedureTemplateStatusBo(Number id, String value, boolean allowsUpdate) {
		this.id = id.shortValue();
		this.value = value;
		this.allowsUpdate = allowsUpdate;
	}

	public EProcedureTemplateStatusBo getNextState() {
		if (this.equals(DRAFT)) return ACTIVE;
		if (this.equals(ACTIVE)) return INACTIVE;
		else return ACTIVE;
	}

	public static EProcedureTemplateStatusBo map(Short id) {
		for(EProcedureTemplateStatusBo e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("procedure-template-status-doesnt-exist", String.format("El tipo estado de plantilla %s no existe", id));
	}

	public static EProcedureTemplateStatusBo getDefault() {
		return DRAFT;
	}

	public boolean isUpdateable() {
		return this.getAllowsUpdate();
	}

}
