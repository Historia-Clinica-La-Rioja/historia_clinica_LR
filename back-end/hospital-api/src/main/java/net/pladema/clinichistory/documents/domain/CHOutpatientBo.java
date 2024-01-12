package net.pladema.clinichistory.documents.domain;

import com.google.common.base.Joiner;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@ToString
public class CHOutpatientBo extends CHDocumentBo{

	private String outpatientConsultationReasons;
	private String problems;
	private String bloodType;
	private String anthropometricData;
	private String riskFactors;
	private String personalRecord;
	private String familyRecord;
	private String medicines;
	private String procedures;
	private String allergies;
	private String note;
	private String outpatientReferences;

	public CHOutpatientBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.outpatientConsultationReasons = entity.getHealthConditionSummary().getConsultationReasons();
		this.problems = entity.getHealthConditionSummary().getProblems();
		this.bloodType = entity.getHealthConditionSummary().getBloodType();
		this.anthropometricData = entity.getHealthConditionSummary().getAnthropometricData();
		this.riskFactors = entity.getHealthConditionSummary().getRiskFactors();
		this.personalRecord = entity.getHealthConditionSummary().getPersonalRecord();
		this.familyRecord = entity.getHealthConditionSummary().getFamilyRecord();
		this.medicines = entity.getHealthConditionSummary().getMedicines();
		this.procedures = entity.getHealthConditionSummary().getProcedures();
		this.allergies = entity.getHealthConditionSummary().getAllergies();
		this.note = entity.getHealthConditionSummary().getNotes();
		this.outpatientReferences = entity.getHealthConditionSummary().getOutpatientReferences();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<String> terms = Stream.of(outpatientConsultationReasons, problems, bloodType, anthropometricData, riskFactors, personalRecord, familyRecord, medicines, allergies, note, outpatientReferences).filter(term -> term!=null && !term.isBlank()).collect(Collectors.toList());
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
