package net.pladema.clinichistory.documents.domain;

public enum ECHEncounterType {

	HOSPITALIZATION ("Internación"),
	EMERGENCY_CARE ("Guardia"),
	OUTPATIENT ("Ambulatorio");

	private String value;
	ECHEncounterType (String value){
		this.value = value;
	}
	public String getValue() {
		return value;
	}

}
