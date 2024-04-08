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
public class CHServiceRequestBo extends CHDocumentBo{

	private String serviceRequestCategory;
	private String serviceRequestStudies;
	private String problems;

	public CHServiceRequestBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.serviceRequestCategory = entity.getHealthConditionSummary().getServiceRequestCategory();
		this.serviceRequestStudies = entity.getHealthConditionSummary().getServiceRequestStudies();
		this.problems = entity.getHealthConditionSummary().getProblems();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<ClinicalRecordBo> result = new ArrayList<>();
		if(serviceRequestStudies!=null && !serviceRequestStudies.isBlank()){
			List<String> terms = Stream.of(serviceRequestCategory, problems.replace('|', ','), serviceRequestStudies).filter(term -> term != null && !term.isBlank()).collect(Collectors.toList());
			terms = terms.stream().map(term -> term.replace("&", "&#38;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&#39;").replace("\"", "&#34;")).collect(Collectors.toList());
			String evolution = Joiner.on(". <br />").join(terms).replace("\\n", ". <br />");
			result.add(new ClinicalRecordBo("Orden", evolution));
		}
		return result;
	}

}
