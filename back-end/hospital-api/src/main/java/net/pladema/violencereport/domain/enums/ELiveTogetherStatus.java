package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ELiveTogetherStatus {

	YES(1, "Si"),
	SAME_SPACE(2, "Si, otra casa en el mismo terreno"),
	NO(3, "No"),
	NOT_NOW(4, "No convive, pero convivió"),
	NO_INFORMATION(5, "Sin información");

	private Short id;

	private String value;

	ELiveTogetherStatus(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ELiveTogetherStatus map(Short id) {
		for (ELiveTogetherStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("live-together-status-not-exists", String.format("El estado %s no existe", id));
	}


}
