package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OutpatientBasicDataBo {
	private Integer id;

	private Long documentId;

	private LocalDate date;

	private PatientBo patient;

	private String clinicalSpecialtySctid;

	private String institutionSisaCode;

	private List<SnomedBo> procedures;

	private List<SnomedBo> problems;

	private AnthropometricDataBo anthropometricData;

	private RiskFactorBo riskFactorData;


	public OutpatientBasicDataBo(Integer id, Long documentId, LocalDate date,
								 String clinicalSpecialtySctid, String institutionSisaCode,
								 Integer patientId, String firstName, String middleNames, String lastName,
								 String otherLastNames, Short identificationType, String identificationNumber,
								 LocalDate birthDate, Short genderId) {
		this.id = id;
		this.documentId = documentId;
		this.date = date;
		this.clinicalSpecialtySctid = clinicalSpecialtySctid;
		this.institutionSisaCode = institutionSisaCode;
		this.patient = new PatientBo(patientId, firstName, middleNames, lastName, otherLastNames,
				identificationType, identificationNumber, birthDate, genderId);
	}
}
