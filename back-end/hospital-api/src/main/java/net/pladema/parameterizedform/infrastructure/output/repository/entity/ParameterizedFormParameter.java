package net.pladema.parameterizedform.infrastructure.output.repository.entity;


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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parameterized_form_parameter")
@Entity
public class ParameterizedFormParameter implements Serializable {

	private static final long serialVersionUID = 167980588094284627L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "parameterized_form_id", nullable = false)
	private Integer parameterizedFormId;

	@Column(name = "parameter_id", nullable = false)
	private Integer parameterId;

	@Column(name = "order_number", nullable = false)
	private Short orderNumber;
	
}
