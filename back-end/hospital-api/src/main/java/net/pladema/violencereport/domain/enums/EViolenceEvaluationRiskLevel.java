package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EViolenceEvaluationRiskLevel {

	LOW(1, "Bajo"),
	MEDIUM(2, "Medio"),
	HIGH(3, "Alto");

	private Short id;

	private String value;

	EViolenceEvaluationRiskLevel(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EViolenceEvaluationRiskLevel map(Short id) {
		for (EViolenceEvaluationRiskLevel e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("violence-evaluation-risk-level-not-exists", String.format("El nivel %s no existe", id));
	}

}
