package ar.lamansys.sgh.publicapi.domain.prescription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class InstitutionPrescriptionBo {
	String name;
	String sisaCode;
	String provinceCode;
	String address;
}
