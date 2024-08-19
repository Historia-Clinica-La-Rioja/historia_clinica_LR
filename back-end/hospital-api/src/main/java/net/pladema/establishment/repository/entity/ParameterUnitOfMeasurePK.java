package net.pladema.establishment.repository.entity;

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
public class ParameterUnitOfMeasurePK implements Serializable {

	private static final long serialVersionUID = 4537311565633076433L;

	@Column(name = "parameter_id", nullable = false)
	private Integer parameterId;

	@Column(name = "unit_of_measure_id", nullable = false)
	private Integer unitOfMeasureId;

}
