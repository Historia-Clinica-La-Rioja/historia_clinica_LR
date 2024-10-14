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
@Table(name = "completed_parameter")
@Entity
public class CompletedParameter implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "completed_parameterized_form_id", nullable = false)
	private Integer completedParameterizedFormId;

	@Column(name = "parameter_id", nullable = false)
	private Integer parameterId;

	@Column(name = "parameter_text_option_id")
	private Integer parameterTextOptionId;

	@Column(name = "numeric_value")
	private Double numericValue;

	@Column(name = "text_value", columnDefinition = "TEXT")
	private String textValue;

	@Column(name = "snomed_id")
	private Integer snomedId;

}
