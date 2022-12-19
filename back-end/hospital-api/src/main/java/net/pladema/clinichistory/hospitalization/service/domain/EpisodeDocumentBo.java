package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDocumentBo {

	private String filePath;
	private String fileName;
	private String uuid;
	private Integer episodeDocumentTypeId;
	private Integer internmentEpisodeId;
}
