package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ElectronicJointSignatureProfessionalsDto {

	List<String> professionalsThatSignedNames;

	private Integer professionalsThatDidNotSignAmount;

}
