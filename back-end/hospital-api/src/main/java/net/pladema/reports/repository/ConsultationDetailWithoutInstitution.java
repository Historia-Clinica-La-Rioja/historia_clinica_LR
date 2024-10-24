package net.pladema.reports.repository;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ConsultationDetailWithoutInstitution {

	private String patientSurname;

	private String patientFirstName;

	private String selfPerceivedName;

	private String identificationType;

	private String identificationNumber;

	private String birthDate;

	private String gender;

	private String address;

	private String phoneNumber;

	private String email;

	private String coverageName;

	private String affiliateNumber;

	private LocalDateTime startDate;

	private Integer clinicalSpecialtyId;

	private String clinicalSpecialty;

	private Integer professionalId;

	private String professionalName;

	private String reasons;

	private String problems;

	private String procedures;

	private String weight;

	private String height;

	private String systolicBloodPressure;

	private String diastolicBloodPressure;

	private String cardiovascularRisk;

	private String glycosylatedHemoglobin;

	private String bloodGlucose;

	private String headCircunference;

	private String cpo;

	private String ceo;

	private Integer hierarchicalUnitId;

	private String hierarchicalUnitAlias;

	private Integer hierarchicalUnitTypeId;

	private String hierarchicalUnitTypeDescription;

}
