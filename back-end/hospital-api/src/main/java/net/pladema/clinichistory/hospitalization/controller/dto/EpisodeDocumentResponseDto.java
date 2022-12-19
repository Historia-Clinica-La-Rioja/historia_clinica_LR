package net.pladema.clinichistory.hospitalization.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDocumentResponseDto {

	private Integer id;
	private String filePath;
	private String fileName;
	private String uuidFile;
	private LocalDate createdOn;
	private Integer episodeDocumentTypeId;
	private Integer internmentEpisodeId;
}
