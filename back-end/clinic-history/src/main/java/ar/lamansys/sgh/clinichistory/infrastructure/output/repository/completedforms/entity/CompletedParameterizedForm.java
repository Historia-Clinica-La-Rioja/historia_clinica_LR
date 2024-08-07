package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.completedforms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
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


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "completed_parameterized_form")
@Entity
public class CompletedParameterizedForm implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "parameterized_form_id", nullable = false)
	private Integer parameterizedFormId;

	@Column(name = "document_id", nullable = false)
	private Long documentId;

	public CompletedParameterizedForm(Integer parameterizedFormId, Long documentId){
		super();
		this.parameterizedFormId = parameterizedFormId;
		this.documentId = documentId;
	}

}
