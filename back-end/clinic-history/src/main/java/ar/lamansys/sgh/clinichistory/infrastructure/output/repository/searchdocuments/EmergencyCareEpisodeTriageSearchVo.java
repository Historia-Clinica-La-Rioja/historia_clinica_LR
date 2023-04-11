package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmergencyCareEpisodeTriageSearchVo {

	private Integer triageId;

	private Long documentId;

	private LocalDateTime createdOn;

	private Integer userId;

	private String responsibleFirstName;

	private String responsibleLastName;

	private String responsibleSelfDeterminedName;

	private String notes;

	private String documentTypeDescription;

}
