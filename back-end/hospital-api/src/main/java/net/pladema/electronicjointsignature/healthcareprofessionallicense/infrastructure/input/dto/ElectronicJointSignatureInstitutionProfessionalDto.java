package net.pladema.electronicjointsignature.healthcareprofessionallicense.infrastructure.input.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ElectronicJointSignatureInstitutionProfessionalDto {

	private Integer healthcareProfessionalId;

	private String completeName;

	private ElectronicJointSignatureLicenseDto license;

	private List<String> clinicalSpecialties;

}
