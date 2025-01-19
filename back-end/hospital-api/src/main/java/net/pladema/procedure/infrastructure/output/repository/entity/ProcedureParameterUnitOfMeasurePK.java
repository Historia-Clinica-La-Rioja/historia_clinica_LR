package net.pladema.procedure.infrastructure.output.repository.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureParameterUnitOfMeasurePK implements Serializable {

	@Column(name = "procedure_parameter_id", nullable = false)
	private Integer procedureParameterId;

	@Column(name = "unit_of_measure_id", nullable = false)
	private Integer unitOfMeasureId;

}
