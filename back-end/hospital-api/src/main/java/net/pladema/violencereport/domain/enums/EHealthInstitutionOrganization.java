package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EHealthInstitutionOrganization {

	MEDICAL_CLINIC(1, "Clínica médica"),
	PEDIATRICS(2, "Pediatría"),
	GYNECOLOGY_OBSTETRICS(3, "Ginecología/obstetricia"),
	SOCIAL_WORK(4, "Trabajo social"),
	MENTAL_HEALTH(5, "Salud mental"),
	NURSING(6, "Enfermería"),
	SAPS(7, "SAPS"),
	EDA(8, "EDA"),
	EMERGENCY_CARE(9, "Guardia"),
	COMMITTEE(10, "Comité contra las violencias"),
	MANAGEMENT(11, "Dirección"),
	OTHERS(12, "Otros"),
	MUNICIPAL_HOSPITAL(13, "Hospital municipal"),
	SAME(14, "SAME");

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
