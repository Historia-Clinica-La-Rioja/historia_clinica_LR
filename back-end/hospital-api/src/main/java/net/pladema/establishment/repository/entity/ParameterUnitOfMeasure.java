package net.pladema.establishment.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Table(name="parameter_unit_of_measure")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class ParameterUnitOfMeasure implements Serializable {

	private static final long serialVersionUID = 7926455284434440720L;

	@EmbeddedId
	private ParameterUnitOfMeasurePK pk;

	public ParameterUnitOfMeasure(Integer parameterId, Integer unitOfMeasureId) {
		this.pk = new ParameterUnitOfMeasurePK(parameterId,unitOfMeasureId);
	}

}
