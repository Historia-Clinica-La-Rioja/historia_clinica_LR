package net.pladema.imagenetwork.infrastructure.output.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "study_pac_association")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyPacAssociation {

	@EmbeddedId
	private StudyPacAssociationPK pk;

	public StudyPacAssociation(String studyInstanceUID, Integer pacServerId) {
		this.pk = new StudyPacAssociationPK(studyInstanceUID, pacServerId);
	}
}
