package ar.lamansys.sgh.shared.domain.medicalcoverage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SharedHealthInsuranceBo extends SharedMedicalCoverageBo {

	private Integer rnos;

	private String acronym;

	public SharedHealthInsuranceBo(Integer id, String name, String cuit, Integer rnos, String acronym, Short type){
		setId(id);
		setName(name);
		setCuit(cuit);
		setType(type);
		this.rnos = rnos;
		this.acronym = acronym;
	}

}
