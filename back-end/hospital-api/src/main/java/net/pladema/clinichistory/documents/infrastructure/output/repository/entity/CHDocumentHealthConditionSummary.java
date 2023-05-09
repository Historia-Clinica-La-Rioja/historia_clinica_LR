package net.pladema.clinichistory.documents.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CHDocumentHealthConditionSummary {

	private String problems;
	private String familyRecord;
	private String personalRecord;
	private String procedures;
	private String medicines;
	private String allergies;
	private String vaccines;
	private String riskFactors;
	private String outpatientConsultationReasons;
	private String odontologyProcedure;
	private String odontologyDiagnostic;

	public List<String> getList(){
		List<String> result = Arrays.asList(problems, familyRecord, personalRecord, medicines, allergies, riskFactors, outpatientConsultationReasons, outpatientConsultationReasons, odontologyProcedure, odontologyDiagnostic, odontologyProcedure);
		return result.stream()
				.filter(healthCondition -> (healthCondition != null && !healthCondition.isBlank()))
				.collect(Collectors.toList());
	}

}
