package ar.lamansys.sgh.publicapi.patient.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InstitutionBo {
	private Integer id;
	private String name;
	private String address;
	private String city;
	private String department;
	private String cuit;
	private String sisaCode;

	private String dependency;
}
