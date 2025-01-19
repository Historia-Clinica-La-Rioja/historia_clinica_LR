package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;


import lombok.AllArgsConstructor;
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

import java.io.Serializable;

@Entity
@Table(name = "document_healthcare_professional")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DocumentHealthcareProfessional implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1606385175258970852L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	@Column(name = "healthcare_professional_id", nullable = false)
	private Integer healthcareProfessionalId;

	@Column(name = "profession_type_id", nullable = false)
	private Short professionTypeId;

	@Column(name = "other_profession_type_description")
	private String otherProfessionTypeDescription;

	@Column(name = "comments", columnDefinition = "TEXT")
	private String comments;
}
