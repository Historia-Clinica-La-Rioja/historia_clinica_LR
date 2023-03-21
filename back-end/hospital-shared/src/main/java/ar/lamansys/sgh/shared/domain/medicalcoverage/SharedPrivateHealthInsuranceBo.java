package ar.lamansys.sgh.shared.domain.medicalcoverage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SharedPrivateHealthInsuranceBo extends SharedMedicalCoverageBo {

	public SharedPrivateHealthInsuranceBo(Integer id, String name, String cuit, Short type){
		super(id, name, cuit, type);
	}

}
