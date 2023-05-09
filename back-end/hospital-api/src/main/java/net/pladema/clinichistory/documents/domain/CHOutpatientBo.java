package net.pladema.clinichistory.documents.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CHOutpatientBo extends CHDocumentBo{

	private String outpatientConsultationReasons;
	private String problems;
	private String riskFactors;
	private String medicines;
	private String procedures;
	private String allergies;

	public CHOutpatientBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.outpatientConsultationReasons = entity.getHealthConditionSummary().getOutpatientConsultationReasons();
		this.problems = entity.getHealthConditionSummary().getProblems();
		this.riskFactors = entity.getHealthConditionSummary().getRiskFactors();
		this.medicines = entity.getHealthConditionSummary().getMedicines();
		this.procedures = entity.getHealthConditionSummary().getProcedures();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<ClinicalRecordBo> result = new ArrayList<>();
		String evolution = "";
		if(outpatientConsultationReasons!=null && !outpatientConsultationReasons.isBlank())
			evolution = evolution.concat(outpatientConsultationReasons);
		if(problems!=null && !problems.isBlank())
			evolution = evolution.concat(problems);
		if(riskFactors!=null && !riskFactors.isBlank())
			evolution = evolution.concat(riskFactors);
		if(medicines!=null && !medicines.isBlank())
			evolution = evolution.concat(medicines);
		if(allergies!=null && !allergies.isBlank())
			evolution = evolution.concat(allergies);
		result.add(new ClinicalRecordBo("Evolución", evolution));
		if(procedures!=null && !procedures.isBlank())
			result.add(new ClinicalRecordBo("Procedimientos", procedures.substring(procedures.indexOf(SPECIAL_CHARACTER)+1)));
		return result;
	}

}
