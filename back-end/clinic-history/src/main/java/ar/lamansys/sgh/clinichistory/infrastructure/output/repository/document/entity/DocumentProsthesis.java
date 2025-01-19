package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "document_prosthesis")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DocumentProsthesis implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "document_id")
	private Long documentId;

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;


}
