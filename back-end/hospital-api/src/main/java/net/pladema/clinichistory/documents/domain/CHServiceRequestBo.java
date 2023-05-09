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
public class CHServiceRequestBo extends CHDocumentBo{

	private String serviceRequestDetails;
	private String serviceRequestStudies;
	private String problems;
	private String serviceRequestResult;

	public CHServiceRequestBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType){
		super(entity, encounterType, documentType);
		this.serviceRequestDetails = entity.getServiceRequestSummary().getServiceRequestDetails();
		this.serviceRequestStudies = entity.getServiceRequestSummary().getServiceRequestStudies();
		this.problems = entity.getHealthConditionSummary().getProblems();
		this.serviceRequestResult = entity.getServiceRequestSummary().getServiceRequestResult();
	}

	@Override
	public List<ClinicalRecordBo> getClinicalRecords() {
		List<ClinicalRecordBo> result = new ArrayList<>();
		if(serviceRequestDetails!=null && !serviceRequestDetails.isBlank() && serviceRequestStudies!=null && !serviceRequestStudies.isBlank() && problems!=null && !problems.isBlank()){
			String serviceRequestSummary = serviceRequestDetails.substring(serviceRequestDetails.indexOf(SPECIAL_CHARACTER)+1, serviceRequestDetails.indexOf("Estado"));
			serviceRequestSummary = serviceRequestSummary.concat(serviceRequestStudies).concat(". ").concat(problems);
			if(serviceRequestResult!= null && !serviceRequestResult.isBlank()) serviceRequestSummary = serviceRequestSummary.concat(". ").concat(serviceRequestResult);
			result.add(new ClinicalRecordBo("Detalles de orden", serviceRequestSummary));
		}
		return result;
	}

}
