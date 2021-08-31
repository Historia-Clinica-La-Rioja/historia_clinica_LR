package ar.lamansys.sgh.shared.infrastructure.input.service.institution;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class InstitutionInfoDto implements Serializable {
	private static final long serialVersionUID = -6806500543924261426L;

	private Integer id;

	private String name;

}
