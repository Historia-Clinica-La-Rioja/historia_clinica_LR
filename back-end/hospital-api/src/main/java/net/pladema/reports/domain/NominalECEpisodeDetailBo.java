package net.pladema.reports.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
public class NominalECEpisodeDetailBo {

	private String institutionProvince;

	private String institutionDepartment;

	private String sisaCode;

	private String institutionName;

	private String hierarchicalUnitType;

	private String hierarchicalUnitAlias;

	private String patientNames;

	private String patientSurnames;

	private String patientSelfPerceivedName;

	private Integer patientId;

	private String identificationType;

	private String identificationNumber;

	private LocalDate birthDate;

	private String gender;

	private String selfPerceivedGender;

	private String quarter;

	private String address;

	private String phone;

	private String email;

	private String episodeCoverageName;

	private String episodeCoverageAffiliateNumber;

	private String patientMedicalCoverages;

	private Integer episodeId;

	private LocalDateTime episodeCreatedOn;

	private String episodeAuthorNames;

	private String episodeAuthorSurnames;

	private String episodeAuthorSelfPerceivedNames;

	private String episodeType;

	private LocalDateTime triageDate;

	private Integer triageQuantity;

	private String triageType;

	private String episodeState;

	private LocalDateTime lastAttentionCreatedOn;

	private String attentionDoctorOffice;

	private String attentionShockroom;

	private String attentionRoom;

	private String attentionBed;

	private String reasons;

	private String diagnosis;

	private LocalDateTime medicalDischargeOn;

	private String procedures;

	private String triageAuthorNames;

	private String triageAuthorSurnames;

	private String triageAuthorSelfPerceivedNames;

	private String encounterProfessionalNames;

	private String encounterProfessionalSurnames;

	private String encounterProfessionalSelfPerceivedNames;

	private String medicalDischargeAuthorNames;

	private String medicalDischargeAuthorSurnames;

	private String medicalDischargeAuthorSelfPerceivedNames;

	private Integer attentionsQuantity;

	private String dischargeType;

	private Integer personId;

	private Integer lastAttentionProfessionalId;

	private String triageAuthor;

	private String encounterProfessional;

	private String medicalDischargeProfessional;

	private String episodeAuthor;

	private String age;

	private String attentionSite;

	public NominalECEpisodeDetailBo(String hierarchicalUnitType, String hierarchicalUnitAlias,
									String patientNames, String patientSurnames, String patientSelfPerceivedName,
									Integer patientId, String identificationType, String identificationNumber, LocalDate birthDate,
									String gender, String selfPerceivedGender, String quarter, String address, String phone,
									String email, String episodeCoverageName, String episodeCoverageAffiliateNumber,
									String patientMedicalCoverages, Integer episodeId,
									LocalDateTime episodeCreatedOn, String episodeAuthorNames, String episodeAuthorSurnames,
									String episodeAuthorSelfPerceivedNames, String episodeType, LocalDateTime triageDate,
									Integer triageQuantity, String triageType, String episodeState,
									LocalDateTime lastAttentionCreatedOn, String attentionDoctorOffice, String attentionShockroom,
									String attentionRoom, String attentionBed, String reasons, String diagnosis,
									LocalDateTime medicalDischargeOn, String procedures, String triageAuthorNames,
									String triageAuthorSurnames, String triageAuthorSelfPerceivedNames,
									String encounterProfessionalNames, String encounterProfessionalSurnames,
									String encounterProfessionalSelfPerceivedNames, String medicalDischargeAuthorNames,
									String medicalDischargeAuthorSurnames, String medicalDischargeAuthorSelfPerceivedNames,
									Integer attentionsQuantity, String dischargeType, Integer personId,
									Integer lastAttentionProfessionalId) {
		this.hierarchicalUnitType = hierarchicalUnitType;
		this.hierarchicalUnitAlias = hierarchicalUnitAlias;
		this.patientNames = patientNames;
		this.patientSurnames = patientSurnames;
		this.patientSelfPerceivedName = patientSelfPerceivedName;
		this.patientId = patientId;
		this.identificationType = identificationType;
		this.identificationNumber = identificationNumber;
		this.birthDate = birthDate;
		this.gender = gender;
		this.selfPerceivedGender = selfPerceivedGender;
		this.address = address;
		this.quarter = quarter;
		this.phone = phone;
		this.email = email;
		this.episodeCoverageName = episodeCoverageName;
		this.episodeCoverageAffiliateNumber = episodeCoverageAffiliateNumber;
		this.patientMedicalCoverages = patientMedicalCoverages;
		this.episodeId = episodeId;
		this.episodeCreatedOn = episodeCreatedOn;
		this.episodeAuthorNames = episodeAuthorNames;
		this.episodeAuthorSurnames = episodeAuthorSurnames;
		this.episodeAuthorSelfPerceivedNames = episodeAuthorSelfPerceivedNames;
		this.episodeType = episodeType;
		this.triageDate = triageDate;
		this.triageQuantity = triageQuantity == 0 ? null : triageQuantity;
		this.triageType = triageType;
		this.episodeState = episodeState;
		this.lastAttentionCreatedOn = lastAttentionCreatedOn;
		this.attentionDoctorOffice = attentionDoctorOffice;
		this.attentionShockroom = attentionShockroom;
		this.attentionRoom = attentionRoom;
		this.attentionBed = attentionBed;
		this.reasons = reasons;
		this.diagnosis = diagnosis;
		this.medicalDischargeOn = medicalDischargeOn;
		this.procedures = procedures;
		this.triageAuthorNames = triageAuthorNames;
		this.triageAuthorSurnames = triageAuthorSurnames;
		this.triageAuthorSelfPerceivedNames = triageAuthorSelfPerceivedNames;
		this.encounterProfessionalNames = encounterProfessionalNames;
		this.encounterProfessionalSurnames = encounterProfessionalSurnames;
		this.encounterProfessionalSelfPerceivedNames = encounterProfessionalSelfPerceivedNames;
		this.medicalDischargeAuthorNames = medicalDischargeAuthorNames;
		this.medicalDischargeAuthorSurnames = medicalDischargeAuthorSurnames;
		this.medicalDischargeAuthorSelfPerceivedNames = medicalDischargeAuthorSelfPerceivedNames;
		this.attentionsQuantity = attentionsQuantity == 0 ? null : attentionsQuantity;
		this.dischargeType = dischargeType;
		this.personId = personId;
		this.lastAttentionProfessionalId = lastAttentionProfessionalId;
	}

}
