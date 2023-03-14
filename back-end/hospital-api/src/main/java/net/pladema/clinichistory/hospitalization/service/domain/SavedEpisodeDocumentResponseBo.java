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
public class SavedEpisodeDocumentResponseBo {

	private Integer id;
	private String filePath;
	private String fileName;
	private String uuidFile;
	private DateDto createdOn;
	private Integer episodeDocumentType;
	private Integer internmentEpisodeId;
}
