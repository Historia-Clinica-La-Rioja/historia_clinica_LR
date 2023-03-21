package ar.lamansys.sgh.shared.domain.medicalcoverage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SharedARTCoverageBo extends SharedMedicalCoverageBo {

	public SharedARTCoverageBo(Integer id, String name, String cuit, Short type){
		setId(id);
		setName(name);
		setCuit(cuit);
		setType(type);
	}

}
