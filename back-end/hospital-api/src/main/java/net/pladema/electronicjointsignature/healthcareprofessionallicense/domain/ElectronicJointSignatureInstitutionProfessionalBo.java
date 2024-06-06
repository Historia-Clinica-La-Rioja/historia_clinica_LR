package net.pladema.electronicjointsignature.healthcareprofessionallicense.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ElectronicJointSignatureInstitutionProfessionalBo {

	private Integer personId;

	private Integer healthcareProfessionalId;

	private String completeName;

	private ElectronicJointSignatureLicenseBo license;

	private List<String> clinicalSpecialties;

}
