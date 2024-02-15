package net.pladema.clinichistory.documents.domain;

import com.google.common.base.Joiner;

import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CHTriageBo extends CHDocumentBo {

	private String riskFactors;
	private String pediatricRiskFactors;
	private String note;

	public CHTriageBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.riskFactors = entity.getHealthConditionSummary().getRiskFactors();
		this.pediatricRiskFactors = entity.getHealthConditionSummary().getPediatricRiskFactors();
		this.note = entity.getHealthConditionSummary().getNotes();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<String> terms = Stream.of(riskFactors, pediatricRiskFactors, note).filter(term -> term != null && !term.isBlank()).collect(Collectors.toList());
		terms = terms.stream().map(term -> term.replace("&", "&#38;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&#39;").replace("\"", "&#34;")).collect(Collectors.toList());
		List<ClinicalRecordBo> result = new ArrayList<>();
		if(!terms.isEmpty()) {
			String evolution = Joiner.on(". <br />").join(terms);
			result.add(new ClinicalRecordBo("Triage", evolution.replace("|(", " (").replace('|', ',').replace("\\n", "<br />")));
		}
		return result;
	}

}
