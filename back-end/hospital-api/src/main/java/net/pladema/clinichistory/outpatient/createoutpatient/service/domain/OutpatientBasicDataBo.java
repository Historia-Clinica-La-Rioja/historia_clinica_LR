package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OutpatientBasicDataBo {
	private Integer id;

	private Long documentId;

	private LocalDateTime date;

	private OutpatientPatientBo patient;

	private String clinicalSpecialtySctid;

	private String institutionSisaCode;

	private List<SnomedBo> procedures;

	private List<SnomedBo> problems;

	private List<SnomedBo> medications;

	private AnthropometricDataBo anthropometricData;

	private RiskFactorBo riskFactorData;


	public OutpatientBasicDataBo(Integer id, Long documentId, LocalDateTime date,
								 String clinicalSpecialtySctid, String institutionSisaCode,
								 Integer patientId, Integer personId, Short identificationType,
								 String identificationNumber, Short genderId) {
		this.id = id;
		this.documentId = documentId;
		this.date = date;
		this.clinicalSpecialtySctid = clinicalSpecialtySctid;
		this.institutionSisaCode = institutionSisaCode;
		this.patient = new OutpatientPatientBo(patientId, personId, identificationType, identificationNumber, genderId);
	}
}
