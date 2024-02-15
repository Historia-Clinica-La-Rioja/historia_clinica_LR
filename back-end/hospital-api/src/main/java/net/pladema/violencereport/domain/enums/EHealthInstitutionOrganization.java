package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EHealthInstitutionOrganization {

	MEDICAL_CLINIC(1, "MEDICAL_CLINIC"),
	PEDIATRICS(2, "PEDIATRICS"),
	GYNECOLOGY_OBSTETRICS(3, "GYNECOLOGY_OBSTETRICS"),
	SOCIAL_WORK(4, "SOCIAL_WORK"),
	MENTAL_HEALTH(5, "MENTAL_HEALTH"),
	NURSING(6, "NURSING"),
	SAPS(7, "SAPS"),
	EDA(8, "EDA"),
	EMERGENCY_CARE(9, "EMERGENCY_CARE"),
	COMMITTEE(10, "COMMITTEE"),
	MANAGEMENT(11, "MANAGEMENT"),
	OTHERS(12, "OTHERS");

	private Short id;

	private String value;

	EHealthInstitutionOrganization(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EHealthInstitutionOrganization map(Short id) {
		for (EHealthInstitutionOrganization e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("health-institution-organization-not-exists", String.format("La organización %s no existe", id));
	}

}
