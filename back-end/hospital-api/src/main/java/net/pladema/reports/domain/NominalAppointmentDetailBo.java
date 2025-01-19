package net.pladema.reports.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

@ToString
@Getter
@Setter
public class NominalAppointmentDetailBo {

	private String province;

	private String department;

	private String sisaCode;

	private String institution;

	private String hierarchicalUnitType;

	private String hierarchicalUnitAlias;

	private String patientNames;

	private String patientSurname;

	private String patientSelfPerceivedName;

	private String identificationType;

	private String identificationNumber;

	private LocalDate birthDate;

	private String selfPerceivedGender;

	private LocalDate appointmentDate;

	private LocalTime appointmentHour;

	private String appointmentState;

	private String address;

	private String phoneNumber;

	private String email;

	private String coverageName;

	private String affiliateNumber;

	private String clinicalSpecialty;

	private String professionalName;

	private String diagnoses;

	private String issuerAppointmentFullName;

	private Integer patientId;

	private Integer diaryHealthcareProfessionalId;

	private Integer appointmentHealthcareProfessionalUserId;

	private Integer appointmentId;

	private Long documentId;

	public NominalAppointmentDetailBo(String hierarchicalUnitType, String hierarchicalUnitAlias, LocalDate appointmentDate,
									  LocalTime appointmentHour, String appointmentState, String phoneNumber, String phonePrefix,
									  String email, String coverageName, String affiliateNumber, String clinicalSpecialty,
									  Integer patientId, Integer appointmentHealthcareProfessionalUserId, Integer diaryHealthcareProfessionalId,
									  Integer appointmentId, Long documentId) {
		this.hierarchicalUnitType = hierarchicalUnitType;
		this.hierarchicalUnitAlias = hierarchicalUnitAlias;
		this.appointmentDate = appointmentDate;
		this.appointmentHour = appointmentHour;
		this.appointmentState = appointmentState;
		if (phoneNumber != null && phonePrefix != null)
			this.phoneNumber = phonePrefix + phoneNumber;
		this.email = email;
		this.coverageName = coverageName;
		this.affiliateNumber = affiliateNumber;
		this.clinicalSpecialty = clinicalSpecialty;
		this.patientId = patientId;
		this.appointmentHealthcareProfessionalUserId = appointmentHealthcareProfessionalUserId;
		this.diaryHealthcareProfessionalId = diaryHealthcareProfessionalId;
		this.appointmentId = appointmentId;
		this.documentId = documentId;
	}

	public void setInstitutionData(Object[] institutionData) {
		this.province = (String) institutionData[0];
		this.department = (String) institutionData[1];
		this.sisaCode = (String) institutionData[2];
		this.institution = (String) institutionData[3];
	}

	public void setPatientData(Object[] patientData) {
		this.patientNames = (String) patientData[1];
		this.patientSurname = (String) patientData[2];
		this.patientSelfPerceivedName = (String) patientData[3];
		this.identificationType = (String) patientData[4];
		this.identificationNumber = (String) patientData[5];
		this.birthDate = ((Date) patientData[6]).toLocalDate();
		this.selfPerceivedGender = (String) patientData[7];
		this.address = (String) patientData[8];
		if (this.phoneNumber == null && patientData[9] != null && patientData[10] != null)
			this.phoneNumber = (String) patientData[9] + patientData[10];
		if (this.email == null)
			this.email = (String) patientData[11];
	}

}
