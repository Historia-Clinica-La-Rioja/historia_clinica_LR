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
public class CHCounterReferenceBo extends CHDocumentBo{

	private String procedures;
	private String medicines;
	private String allergies;

	public CHCounterReferenceBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.procedures = entity.getHealthConditionSummary().getProblems();
		this.medicines = entity.getHealthConditionSummary().getMedicines();
		this.allergies = entity.getHealthConditionSummary().getAllergies();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<ClinicalRecordBo> result = new ArrayList<>();
		String evolution = "";
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

