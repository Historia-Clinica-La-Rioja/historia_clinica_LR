package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "document")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Document extends SGXAuditableEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "source_id", nullable = false)
	private Integer sourceId;

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

	@Column(name = "source_type_id", nullable = false)
	private Short sourceTypeId;

	@Column(name = "modification_reason", columnDefinition = "TEXT")
	private String modificationReason;

	@Column(name = "initial_document_id")
	private Long initialDocumentId;

	@Column(name = "patient_age_period")
	private String patientAgePeriod;

	public Document(Integer sourceId, String statusId, Short typeId, Short sourceTypeId, Long initialDocumentId) {
		this.sourceId = sourceId;
		this.statusId = statusId;
		this.typeId = typeId;
		this.sourceTypeId = sourceTypeId;
		this.initialDocumentId = initialDocumentId;
	}

	public Document(Integer sourceId, String statusId, Short typeId, Short sourceTypeId) {
		this(sourceId, statusId, typeId, sourceTypeId, null);
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
				sourceId.equals(document.sourceId) &&
				sourceTypeId.equals(document.sourceTypeId) &&
				typeId.equals(document.typeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, sourceId, typeId, sourceTypeId);
	}
}
