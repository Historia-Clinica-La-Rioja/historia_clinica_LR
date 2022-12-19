package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDocumentResponseBo {

	private Integer id;
	private String filePath;
	private String fileName;
	private String uuidFile;
	private LocalDate createdOn;
	private Integer episodeDocumentTypeId;
	private Integer internmentEpisodeId;
}
