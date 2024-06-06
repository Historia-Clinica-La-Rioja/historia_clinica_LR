package net.pladema.clinichistory.cipres.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientPatientBo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CipresOutpatientBasicDataBo {

	private Integer id;

	private Long documentId;

	private LocalDateTime date;

	private OutpatientPatientBo patient;

	private Integer clinicalSpecialtyId;

	private String clinicalSpecialtySctid;

	private Integer institutionId;

	private String institutionSisaCode;

	private List<SnomedBo> procedures;

	private List<SnomedBo> problems;

	private List<SnomedBo> medications;

	private AnthropometricDataBo anthropometricData;

	private RiskFactorBo riskFactorData;


	public CipresOutpatientBasicDataBo(Integer id, Long documentId, LocalDateTime date, Integer clinicalSpecialtyId,
									   String clinicalSpecialtySctid, Integer institutionId, String institutionSisaCode,
									   Integer patientId, Integer personId, Short identificationType,
									   String identificationNumber, Short genderId) {
		this.id = id;
		this.documentId = documentId;
		this.date = date;
		this.clinicalSpecialtyId = clinicalSpecialtyId;
		this.clinicalSpecialtySctid = clinicalSpecialtySctid;
		this.institutionId = institutionId;
		this.institutionSisaCode = institutionSisaCode;
		this.patient = new OutpatientPatientBo(patientId, personId, identificationType, identificationNumber, genderId);
	}

}
