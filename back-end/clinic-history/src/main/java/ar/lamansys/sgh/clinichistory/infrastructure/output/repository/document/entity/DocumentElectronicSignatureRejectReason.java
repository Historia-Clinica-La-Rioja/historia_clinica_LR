package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document_electronic_signature_reject_reason")
@Entity
public class DocumentElectronicSignatureRejectReason {

	@Id
	@Column(name = "document_involved_professional_id")
	private Integer documentInvolvedProfessionalId;

	@Column(name = "reason_id", nullable = false)
	private Short reasonId;

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;

}
