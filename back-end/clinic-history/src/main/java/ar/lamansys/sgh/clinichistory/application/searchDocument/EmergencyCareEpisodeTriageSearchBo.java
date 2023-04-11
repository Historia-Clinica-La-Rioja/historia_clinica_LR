package ar.lamansys.sgh.clinichistory.application.searchDocument;

import ar.lamansys.sgh.clinichistory.domain.ips.OtherRiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.EmergencyCareEpisodeTriageSearchVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class EmergencyCareEpisodeTriageSearchBo {

	private Integer triageId;

	private Long documentId;

	private LocalDateTime createdOn;

	private Integer userId;

	private String responsibleFirstName;

	private String responsibleLastName;

	private String responsibleSelfDeterminedName;

	private String notes;

	private String documentTypeDescription;

	private RiskFactorBo riskFactors;

	private OtherRiskFactorBo otherRiskFactors;

	public EmergencyCareEpisodeTriageSearchBo(EmergencyCareEpisodeTriageSearchVo emergencyCareEpisodeTriageSearch) {
		this.triageId = emergencyCareEpisodeTriageSearch.getTriageId();
		this.documentId = emergencyCareEpisodeTriageSearch.getDocumentId();
		this.createdOn = emergencyCareEpisodeTriageSearch.getCreatedOn();
		this.userId = emergencyCareEpisodeTriageSearch.getUserId();
		this.responsibleFirstName = emergencyCareEpisodeTriageSearch.getResponsibleFirstName();
		this.responsibleLastName = emergencyCareEpisodeTriageSearch.getResponsibleLastName();
		this.responsibleSelfDeterminedName = emergencyCareEpisodeTriageSearch.getResponsibleSelfDeterminedName();
		this.notes = emergencyCareEpisodeTriageSearch.getNotes();
		this.documentTypeDescription = emergencyCareEpisodeTriageSearch.getDocumentTypeDescription();
	}

}
