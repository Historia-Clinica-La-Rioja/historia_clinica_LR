package net.pladema.clinichistory.documents.domain;

import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import java.util.ArrayList;
import java.util.List;

public class CHIndicationBo extends CHDocumentBo {

	private String indication;

	public CHIndicationBo (VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.indication = entity.getHealthConditionSummary().getIndication();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<ClinicalRecordBo> result = new ArrayList<>();
		if(indication!=null && !indication.isBlank()) result.add(new ClinicalRecordBo("Indicación", indication.replace("\\n", ".<br />")));
		return result;
	}

}
