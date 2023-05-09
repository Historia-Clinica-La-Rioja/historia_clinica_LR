package net.pladema.clinichistory.documents.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CHOdontologyBo extends CHDocumentBo{

	private String problems;
	private String evolutionNote;
	private String procedures;
	private String medicines;
	private String odontologyDiagnostic;
	private String odontologyProcedure;

	public CHOdontologyBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.problems = entity.getHealthConditionSummary().getProblems();
		this.evolutionNote = entity.getNotes().getOtherNote();
		this.procedures = entity.getHealthConditionSummary().getProcedures();
		this.medicines = entity.getHealthConditionSummary().getMedicines();
		this.odontologyDiagnostic = entity.getHealthConditionSummary().getOdontologyDiagnostic();
		this.odontologyProcedure = entity.getHealthConditionSummary().getOdontologyProcedure();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<ClinicalRecordBo> result = new ArrayList<>();
		String evolution = "";
		if(problems!=null && !problems.isBlank())
			evolution = evolution.concat(problems);
		if(evolutionNote!=null && !evolutionNote.isBlank())
			evolution = evolution.concat(". Evolución:").concat(evolutionNote.substring(evolutionNote.indexOf(SPECIAL_CHARACTER)));
		if(medicines!=null && !medicines.isBlank())
			evolution = evolution.concat(". ").concat(medicines);
		if(odontologyDiagnostic!=null && !odontologyDiagnostic.isBlank())
			evolution = evolution.concat(". ").concat(odontologyDiagnostic);
		if(odontologyProcedure!=null && !odontologyProcedure.isBlank())
			evolution = evolution.concat(". ").concat(odontologyProcedure);
		result.add(new ClinicalRecordBo("Evolución", evolution));
		if (procedures!=null && !procedures.isBlank())
			result.add(new ClinicalRecordBo("Procedimientos", procedures.substring(procedures.indexOf(SPECIAL_CHARACTER)+1)));
		return result;
	}


}
