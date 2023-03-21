package net.pladema.clinichistory.hospitalization.service.domain;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDocumentResponseBo {

	private Integer id;
	private String description;
	private String fileName;
	private DateDto createdOn;
}
