package net.pladema.clinichistory.hospitalization.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EpisodeDocumentDto {

	private MultipartFile file;
	private Integer episodeDocumentTypeId;
	private Integer internmentEpisodeId;
	private Integer consentId;
}
