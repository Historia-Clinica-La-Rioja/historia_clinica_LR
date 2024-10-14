package net.pladema.imagenetwork.infrastructure.output.database.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

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
@Table(name = "study_pac_association")
@Entity
public class StudyPacAssociation {

	@EmbeddedId
	private StudyPacAssociationPK pk;

	public StudyPacAssociation(String studyInstanceUID, Integer pacServerId) {
		this.pk = new StudyPacAssociationPK(studyInstanceUID, pacServerId);
	}
}
