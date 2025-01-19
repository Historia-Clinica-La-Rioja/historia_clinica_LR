package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EMunicipalGovernmentDevice {

	GENDER_DIVERSITY(1, "Área de género y diversidad"),
	LOCAL_COMMITTEE_AGAINST_VIOLENCE(2, "Mesa local contra las violencias"),
	PROTECTION_CHILDREN_TEENS(3, "Servicio local de promoción y protección de derechos de NNyA"),
	DIRECTORATE_CHILDHOOD(4, "Dirección/área de niñez y adolescencia"),
	SOCIAL_DEVELOPMENT_AREA(5, "Área de desarrollo social"),
	PREVENTION_TREATMENT(6, "Dirección/área de prevención y atención de adicciones"),
	EDUCATIONAL_INSTITUTION(7, "Instituciones educativas"),;

	private Short id;

	private String value;

	EMunicipalGovernmentDevice(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EMunicipalGovernmentDevice map(Short id) {
		for (EMunicipalGovernmentDevice e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("municipal-state-option-not-exists", String.format("La opción %s no existe", id));
	}

}
