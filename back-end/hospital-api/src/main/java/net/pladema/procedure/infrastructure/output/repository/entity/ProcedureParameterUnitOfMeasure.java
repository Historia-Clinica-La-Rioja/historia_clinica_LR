package net.pladema.procedure.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "procedure_parameter_unit_of_measure")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProcedureParameterUnitOfMeasure implements Serializable {

	@EmbeddedId
	private ProcedureParameterUnitOfMeasurePK pk;

	public ProcedureParameterUnitOfMeasure(Integer procedureParameterId, Integer unitOfMeasureId) {
		this.pk = new ProcedureParameterUnitOfMeasurePK(procedureParameterId,unitOfMeasureId);
	}

}
