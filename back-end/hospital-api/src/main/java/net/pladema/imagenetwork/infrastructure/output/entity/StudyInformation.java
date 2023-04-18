package net.pladema.imagenetwork.infrastructure.output.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.PacServer;

@Entity
@Table(name = "study_information")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyInformation {

	@Id
	@Column(name = "id")
	private String studyInstanceUID;

	@ManyToMany
	@JoinTable(
			name = "study_pac_association",
			joinColumns = @JoinColumn(name = "study_id", referencedColumnName = "id", nullable = false),
			inverseJoinColumns = @JoinColumn(name = "pac_server_id", referencedColumnName = "id", nullable = false)
	)
	private Set<PacServer> pacs;
}
