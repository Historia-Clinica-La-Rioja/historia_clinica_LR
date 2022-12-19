package net.pladema.clinichistory.hospitalization.infrastructure.output.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "episode_document")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDocument {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "file_path", nullable = false)
	private String filePath;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "uuid_file", nullable = false)
	private String uuidFile;

	@Column(name = "created_on")
	private LocalDate createdOn = LocalDate.now();

	@Column(name = "episode_document_types_id", nullable = false)
	private Integer episodeDocumentTypeId;

	@Column(name = "internment_episode_id", nullable = false)
	private Integer internmentEpisodeId;

	public EpisodeDocument(String filePath, String fileName, String uuid, Integer episodeDocumentTypeId, Integer internmentEpisodeId) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.uuidFile = uuid;
		this.episodeDocumentTypeId = episodeDocumentTypeId;
		this.internmentEpisodeId = internmentEpisodeId;
	}
}
