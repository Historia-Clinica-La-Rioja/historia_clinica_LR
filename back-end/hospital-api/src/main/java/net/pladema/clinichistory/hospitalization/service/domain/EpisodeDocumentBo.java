package net.pladema.clinichistory.hospitalization.service.domain;

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
public class EpisodeDocumentBo {
	private String originalFilename;

	private Integer episodeDocumentTypeId;
	private Integer internmentEpisodeId;
}
