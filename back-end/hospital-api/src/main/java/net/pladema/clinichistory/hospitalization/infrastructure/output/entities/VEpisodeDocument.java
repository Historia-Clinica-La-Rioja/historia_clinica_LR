package net.pladema.clinichistory.hospitalization.infrastructure.output.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "v_episode_document")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VEpisodeDocument {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "created_on", nullable = false)
	private LocalDate createdOn;

	@Column(name = "internment_episode_id")
	private Integer internmentEpisodeId;

}
