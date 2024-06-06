package net.pladema.clinichistory.documents.domain;

public enum ECHDocumentType {

	EPICRISIS ("Epicrisis"),
	REPORTS ("Informes"),
	MEDICAL_PRESCRIPTIONS ("Prescripciones Médicas"),
	CLINICAL_NOTES ("Notas Clínicas"),
	OTHER ("Otro"),

	NOT_SUPPORTED("No soportado");

	private String value;
	ECHDocumentType (String value){
		this.value = value;
	}

	public String getValue(){
		return this.value;
	}

}
