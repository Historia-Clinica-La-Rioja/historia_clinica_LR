package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum EPharmacoFoodRelation {

	NO(1, "No"),
	LEJOS(2, "Lejos"),
	AYUNO(3, "Ayuno");


	private Short id;
	private String value;

	EPharmacoFoodRelation(Number id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Short getId() {
		return id;
	}

	public static EPharmacoFoodRelation map(Short id) {
		for (EPharmacoFoodRelation e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("pharmaco-food-relation-not-exists", String.format("La relación con comida  %s no existe", id));
	}
}
