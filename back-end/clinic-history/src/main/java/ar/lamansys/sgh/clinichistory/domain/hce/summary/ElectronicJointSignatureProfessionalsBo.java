package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ElectronicJointSignatureProfessionalsBo {

	List<String> professionalsThatSignedNames;

	private Integer professionalsThatDidNotSignAmount;

}
