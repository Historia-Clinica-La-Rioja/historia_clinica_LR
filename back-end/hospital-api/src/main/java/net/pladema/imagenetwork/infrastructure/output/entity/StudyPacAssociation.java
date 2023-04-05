package net.pladema.imagenetwork.infrastructure.output.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.PacServer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "study_pac_association")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyPacAssociation {

	@Id
	@Column(name = "study_id")
	private String studyInstanceUID;

	@ManyToOne
	@JoinColumn(name = "pac_server_id")
	private PacServer pacGlobal;
}
