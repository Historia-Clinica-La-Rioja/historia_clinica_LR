package net.pladema.clinichistory.documents.domain;

import ca.uhn.fhir.rest.annotation.Search;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CHNursingConsultationBo extends CHDocumentBo {

	private String riskFactors;
	private String problems;
	private String procedures;

	public CHNursingConsultationBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.riskFactors = entity.getHealthConditionSummary().getRiskFactors();
		this.problems = entity.getHealthConditionSummary().getProblems();
		this.procedures = entity.getHealthConditionSummary().getProcedures();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<ClinicalRecordBo> result = new ArrayList<>();
		String evolution = "";
		if(problems!=null && !problems.isBlank())
			evolution = evolution.concat(problems);
		if(riskFactors!=null && !riskFactors.isBlank());
			evolution = evolution.concat(riskFactors);
		result.add(new ClinicalRecordBo("Evolución", evolution));
		if(procedures!=null &&  !procedures.isBlank())
			result.add(new ClinicalRecordBo("Procedimientos", procedures.substring(procedures.indexOf(SPECIAL_CHARACTER)+1)));
		return result;
	}

}
