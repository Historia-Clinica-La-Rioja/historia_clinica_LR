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
public class CHCounterReferenceBo extends CHDocumentBo{

	private String problems;
	private String referenceCounterReference;
	private String procedures;
	private String medicines;
	private String allergies;
	private String counterReferenceClosure;

	public CHCounterReferenceBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.problems = entity.getHealthConditionSummary().getProblems();
		this.referenceCounterReference = entity.getHealthConditionSummary().getReferenceCounterReference();
		this.procedures = entity.getHealthConditionSummary().getProcedures();
		this.medicines = entity.getHealthConditionSummary().getMedicines();
		this.allergies = entity.getHealthConditionSummary().getAllergies();
		this.counterReferenceClosure = entity.getHealthConditionSummary().getCounterReferenceClosure();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<String> terms = Stream.of(counterReferenceClosure, procedures, medicines, allergies, referenceCounterReference, problems).filter(term-> term!=null && !term.isBlank()).collect(Collectors.toList());
		terms = terms.stream().map(term -> term.replace("&", "&#38;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&#39;").replace("\"", "&#34;")).collect(Collectors.toList());
		List<ClinicalRecordBo> result = new ArrayList<>();
		if(!terms.isEmpty()) {
			String evolution = Joiner.on(". <br />").join(terms).replace('|', ',').replace("\\n", "<br />");
			result.add(new ClinicalRecordBo("Respuesta a referencia", evolution));
		}
		return result;
	}

}

