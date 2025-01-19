package net.pladema.clinichistory.documents.domain;

import com.google.common.base.Joiner;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class CHOdontologyBo extends CHDocumentBo{

	private String problems;
	private String note;
	private String procedures;
	private String personalRecord;
	private String allergies;
	private String medicines;
	private String odontologyDiagnostic;
	private String odontologyProcedure;
	private String odontologyPieces;
	private String outpatientReferences;

	public CHOdontologyBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.problems = entity.getHealthConditionSummary().getProblems();
		this.note = entity.getHealthConditionSummary().getNotes();
		this.procedures = entity.getHealthConditionSummary().getProcedures();
		this.personalRecord = entity.getHealthConditionSummary().getPersonalRecord();
		this.allergies = entity.getHealthConditionSummary().getAllergies();
		this.medicines = entity.getHealthConditionSummary().getMedicines();
		this.odontologyDiagnostic = entity.getHealthConditionSummary().getOdontologyDiagnostic();
		this.odontologyProcedure = entity.getHealthConditionSummary().getOdontologyProcedure();
		this.odontologyPieces = entity.getHealthConditionSummary().getOdontologyPieces();
		this.outpatientReferences = entity.getHealthConditionSummary().getOutpatientReferences();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<String> terms = Stream.of(problems, note, personalRecord, allergies, medicines, odontologyDiagnostic, odontologyProcedure, odontologyPieces, outpatientReferences).filter(term -> term!=null && !term.isBlank()).collect(Collectors.toList());
		terms = terms.stream().map(term -> term.replace("&", "&#38;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&#39;").replace("\"", "&#34;")).collect(Collectors.toList());
		List<ClinicalRecordBo> result = new ArrayList<>();
		if(!terms.isEmpty()) {
			String evolution = Joiner.on(". <br />").join(terms);
			result.add(new ClinicalRecordBo("Evolución", evolution.replace('|', ',').replace("\\n", "<br />")));
		}
		if (procedures != null && !procedures.isBlank())
			result.add(new ClinicalRecordBo("Procedimientos", procedures.substring(procedures.indexOf(SPECIAL_CHARACTER)+1)));
		return result;
	}

}
