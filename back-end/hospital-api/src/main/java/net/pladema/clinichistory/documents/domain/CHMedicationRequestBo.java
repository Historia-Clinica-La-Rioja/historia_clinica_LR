package net.pladema.clinichistory.documents.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CHMedicationRequestBo extends CHDocumentBo{

	private String medicines;

	public CHMedicationRequestBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.medicines = entity.getHealthConditionSummary().getMedicines();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<ClinicalRecordBo> result = new ArrayList<>();
		if(medicines!=null && !medicines.isBlank()){
			String description = medicines.replace("&", "&#38;")
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
