package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document_involved_professional")
@Entity
public class DocumentInvolvedProfessional implements Serializable {

	private static final long serialVersionUID = 1121013981500516374L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "healthcare_professional_id", nullable = false)
	private Integer healthcareProfessionalId;

	@Column(name = "signature_status_id")
	private Short signatureStatusId;

	@Column(name = "status_update_date")
	private LocalDate statusUpdateDate;

}
