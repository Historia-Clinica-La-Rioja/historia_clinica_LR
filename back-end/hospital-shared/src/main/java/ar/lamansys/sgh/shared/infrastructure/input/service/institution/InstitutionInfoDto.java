package ar.lamansys.sgh.shared.infrastructure.input.service.institution;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InstitutionInfoDto implements Serializable {
	private static final long serialVersionUID = -6806500543924261426L;

	private Integer id;

	private String name;

	private String sisaCode;

	public InstitutionInfoDto(Integer id, String name, String sisaCode) {
		this.id = id;
		this.name = name;
		this.sisaCode = sisaCode;
	}
}
