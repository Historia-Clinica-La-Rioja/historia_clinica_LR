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
public class CHNursingConsultationBo extends CHDocumentBo {

	private String bloodType;
	private String anthropometricData;
	private String riskFactors;
	private String problems;
	private String note;
	private String procedures;

	public CHNursingConsultationBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.bloodType = entity.getHealthConditionSummary().getBloodType();
		this.anthropometricData = entity.getHealthConditionSummary().getAnthropometricData();
		this.riskFactors = entity.getHealthConditionSummary().getRiskFactors();
		this.problems = entity.getHealthConditionSummary().getProblems();
		this.note = entity.getHealthConditionSummary().getNotes();
		this.procedures = entity.getHealthConditionSummary().getProcedures();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<String> terms = Stream.of(bloodType.replace("−", "&ndash;"), anthropometricData, riskFactors, problems, note).filter(term-> term!=null && !term.isBlank()).collect(Collectors.toList());
		List<ClinicalRecordBo> result = new ArrayList<>();
		if(!terms.isEmpty()) {
			String evolution = Joiner.on(". <br />").join(terms);
			result.add(new ClinicalRecordBo("Evolución", evolution.replace('|', ',')));
		}
		if (procedures != null && !procedures.isBlank())
			result.add(new ClinicalRecordBo("Procedimientos", procedures.substring(procedures.indexOf(SPECIAL_CHARACTER)+1)));
		return result;
	}

}
