package net.pladema.internation.repository.documents.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.listener.InternationAuditableEntity;
import net.pladema.internation.repository.listener.InternationListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "document")
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Document extends InternationAuditableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "internment_episode_id", nullable = false)
	private Integer internmentEpisodeId;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId;

	@Column(name = "other_note_id")
	private Long otherNoteId;

	@Column(name = "physical_exam_note_id")
	private Long physicalExamNoteId;

	@Column(name = "studies_summary_note_id")
	private Long studiesSummaryNoteId;

	@Column(name = "evolution_note_id")
	private Long evolutionNoteId;

	@Column(name = "clinical_impression_note_id")
	private Long clinicalImpressionNoteId;

	@Column(name = "current_illness_note_id")
	private Long currentIllnessNoteId;

	@Column(name = "indications_note_id")
	private Long indicationsNoteId;

	@Column(name = "type_id", nullable = false)
	private Short typeId;

	public Document(Integer internmentEpisodeId, String statusId, Short typeId) {
		this.internmentEpisodeId = internmentEpisodeId;
		this.statusId = statusId;
		this.typeId = typeId;
	}

	public boolean isType(short type){
		return this.typeId.equals(type);
	}

	public boolean hasStatus(String statusId){
		return this.statusId.equals(statusId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Document document = (Document) o;
		return id.equals(document.id) &&
				internmentEpisodeId.equals(document.internmentEpisodeId) &&
				typeId.equals(document.typeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, internmentEpisodeId, typeId);
	}
}
