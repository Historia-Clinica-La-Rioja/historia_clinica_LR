package net.pladema.clinichistory.documents.domain;

import com.google.common.base.Joiner;

import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CHEvolutionNoteBo extends CHDocumentBo{

	private String reasons;
	private String problems;
	private String notes;
	private String bloodType;
	private String anthropometricData;
	private String familyRecord;
	private String medicines;
	private String riskFactors;
	private String allergies;
	private String vaccines;
	private String procedures;

	public CHEvolutionNoteBo (VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.reasons = entity.getHealthConditionSummary().getConsultationReasons();
		this.problems = entity.getHealthConditionSummary().getProblems();
		this.notes = entity.getHealthConditionSummary().getNotes();
		this.bloodType = entity.getHealthConditionSummary().getBloodType();
		this.anthropometricData = entity.getHealthConditionSummary().getAnthropometricData();
		this.familyRecord = entity.getHealthConditionSummary().getFamilyRecord();
		this.medicines = entity.getHealthConditionSummary().getMedicines();
		this.riskFactors = entity.getHealthConditionSummary().getRiskFactors();
		this.allergies = entity.getHealthConditionSummary().getAllergies();
		this.vaccines = entity.getHealthConditionSummary().getVaccines();
		this.procedures = entity.getHealthConditionSummary().getProcedures();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<String> terms = Stream.of(reasons, problems, notes, bloodType, anthropometricData, familyRecord, medicines, riskFactors, allergies, vaccines).filter(term -> term!=null && !term.isBlank()).collect(Collectors.toList());
		terms = terms.stream().map(term -> term.replace("&", "&#38;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&#39;").replace("\"", "&#34;").replace("−", "&ndash;")).collect(Collectors.toList());
		List<ClinicalRecordBo> result = new ArrayList<>();
		if(!terms.isEmpty()) {
			String evolution = Joiner.on(". <br />").join(terms);
			result.add(new ClinicalRecordBo("Evolución", evolution.replace("|(", " (").replace('|', ',').replace("\\n", "<br />")));
		}
		if(procedures!=null && !procedures.isBlank())
			result.add(new ClinicalRecordBo("Procedimientos", procedures.substring(procedures.indexOf(SPECIAL_CHARACTER)+1)));
		return result;
	}

}
