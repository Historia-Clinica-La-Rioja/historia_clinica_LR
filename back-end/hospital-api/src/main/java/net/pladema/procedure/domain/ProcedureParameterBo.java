package net.pladema.procedure.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProcedureParameterBo {

	private Integer id;

	private Integer procedureTemplateId;

	private Integer loincId;

	private Short orderNumber;

	private Short typeId;

	private Short inputCount;

}
