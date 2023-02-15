package ar.lamansys.sgh.shared.domain.medicalcoverage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SharedMedicalCoverageBo implements Serializable {

	private Integer id;

	private String name;

	private String cuit;

	private Short type;

}
