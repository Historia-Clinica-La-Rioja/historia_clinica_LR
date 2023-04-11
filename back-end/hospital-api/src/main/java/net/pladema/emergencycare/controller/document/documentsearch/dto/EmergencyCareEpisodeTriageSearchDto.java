package net.pladema.emergencycare.controller.document.documentsearch.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.OtherRiskFactorDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyCareEpisodeTriageSearchDto {

	private Integer triageId;

	private Long documentId;

	private LocalDateTime createdOn;

	private Integer userId;

	private String responsibleFirstName;

	private String responsibleLastName;

	private String responsibleSelfDeterminedName;

	private String notes;

	private String documentTypeDescription;

	private RiskFactorDto riskFactors;

	private OtherRiskFactorDto otherRiskFactors;

}
