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

	private String address;

	private String phone;

	private String email;

	public InstitutionInfoDto(Integer id, String name, String sisaCode) {
		this.id = id;
		this.name = name;
		this.sisaCode = sisaCode;
	}

	public InstitutionInfoDto(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public InstitutionInfoDto(Integer id, String name, String phone, String email) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.email = email;
	}
	
	public InstitutionInfoDto(Integer id, String name, String sisaCode, String address, String phone, String email) {
		this.id = id;
		this.name = name;
		this.sisaCode = sisaCode;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}
}
