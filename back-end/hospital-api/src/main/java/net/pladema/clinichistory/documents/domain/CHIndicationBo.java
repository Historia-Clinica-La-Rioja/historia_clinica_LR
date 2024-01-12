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
		if(indication!=null && !indication.isBlank()){
			String description = indication.replace("&", "&#38;")
					.replace("<", "&lt;")
					.replace(">", "&gt;")
					.replace("'", "&#39;")
					.replace("\"", "&#34;")
					.replace("\\n", ".<br />");
			result.add(new ClinicalRecordBo("Indicación", description));
		}
		return result;
	}

}
