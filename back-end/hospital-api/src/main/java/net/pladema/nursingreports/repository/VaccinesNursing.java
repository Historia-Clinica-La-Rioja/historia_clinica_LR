package net.pladema.nursingreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class VaccinesNursing {

	private String institution;

	private String operativeUnit;

	private String provider;

	private String providerIdentificationNumber;

	private String attentionDate;

	private String patientIdentificationNumber;

	private String patientName;

	private String patientGender;

	private String patientBirthDate;

	private String ageOnAppointmentDate;

	private String vaccine;

	private String sctid;

	private String cie10Code;

	private String status;

	private String condition;

	private String vaccinationSchedule;

	private String vaccinationDosage;

	private String vaccinationLot;

	public VaccinesNursing ( VaccinesNursing institution){
		this.institution = institution.getInstitution();
	}
}

