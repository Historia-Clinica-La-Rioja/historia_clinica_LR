package net.pladema.clinichistory.documents.domain;

import com.google.common.base.Joiner;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import org.apache.logging.log4j.util.Strings;

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
		List<String> terms = Stream.of(referenceCounterReference, problems, medicines, allergies, counterReferenceClosure).filter(term-> term!=null && !term.isBlank()).collect(Collectors.toList());
		List<ClinicalRecordBo> result = new ArrayList<>();
		if(!terms.isEmpty()) {
			String evolution = Joiner.on(". <br />").join(terms).replace('|', ',').replace("\\n", "<br />");
			result.add(new ClinicalRecordBo("Respuesta a referencia", evolution));
		}
		if (procedures != null && !procedures.isBlank())
			result.add(new ClinicalRecordBo("Procedimientos", procedures.substring(procedures.indexOf(SPECIAL_CHARACTER) + 1)));
		return result;
	}

}

