package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ECriminalRecordStatus {

	YES(1, "Si"),
	WITH_OTHER_PEOPLE(2, "Si, con otras personas"),
	NO(3, "No"),
	NO_INFORMATION(4, "Sin información");

	private Short id;

	private String value;

	ECriminalRecordStatus(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ECriminalRecordStatus map(Short id) {
		for (ECriminalRecordStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("criminal-record-status-not-exists", String.format("El estado %s no existe", id));
	}


}
