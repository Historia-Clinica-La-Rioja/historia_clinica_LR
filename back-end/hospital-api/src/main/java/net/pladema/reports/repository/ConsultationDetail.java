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
public class ConsultationDetail {

	private String province;

	private String department;

	private String sisaCode;

	private String institution;

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

	public ConsultationDetail(ConsultationDetailWithoutInstitution consultation){
		this.patientSurname = consultation.getPatientSurname();
		this.patientFirstName = consultation.getPatientFirstName();
		this.selfPerceivedName = consultation.getSelfPerceivedName();
		this.identificationType = consultation.getIdentificationType();
		this.identificationNumber = consultation.getIdentificationNumber();
		this.birthDate = consultation.getBirthDate();
		this.gender = consultation.getGender();
		this.address = consultation.getAddress();
		this.phoneNumber = consultation.getPhoneNumber();
		this.email = consultation.getEmail();
		this.coverageName = consultation.getCoverageName();
		this.affiliateNumber = consultation.getAffiliateNumber();
		this.startDate = consultation.getStartDate();
		this.clinicalSpecialtyId = consultation.getClinicalSpecialtyId();
		this.clinicalSpecialty = consultation.getClinicalSpecialty();
		this.professionalId = consultation.getProfessionalId();
		this.professionalName = consultation.getProfessionalName();
		this.reasons = consultation.getReasons();
		this.problems = consultation.getProblems();
		this.procedures = consultation.getProcedures();
		this.weight = consultation.getWeight();
		this.height = consultation.getHeight();
		this.systolicBloodPressure = consultation.getSystolicBloodPressure();
		this.diastolicBloodPressure = consultation.getDiastolicBloodPressure();
		this.cardiovascularRisk = consultation.getCardiovascularRisk();
		this.glycosylatedHemoglobin = consultation.getGlycosylatedHemoglobin();
		this.bloodGlucose = consultation.getBloodGlucose();
		this.headCircunference = consultation.getHeadCircunference();
		this.cpo = consultation.getCpo();
		this.ceo = consultation.getCeo();
		this.hierarchicalUnitId = consultation.getHierarchicalUnitId();
		this.hierarchicalUnitAlias = consultation.getHierarchicalUnitAlias();
		this.hierarchicalUnitTypeId = consultation.getHierarchicalUnitTypeId();
		this.hierarchicalUnitTypeDescription = consultation.getHierarchicalUnitTypeDescription();
	}
}
