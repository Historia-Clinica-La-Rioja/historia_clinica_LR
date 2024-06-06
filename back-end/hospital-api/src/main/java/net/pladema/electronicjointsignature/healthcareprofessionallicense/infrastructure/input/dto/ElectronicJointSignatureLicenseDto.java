package net.pladema.electronicjointsignature.healthcareprofessionallicense.infrastructure.input.dto;

import lombok.Getter;

import lombok.NoArgsConstructor;

import lombok.Setter;
import lombok.ToString;

import net.pladema.sisa.refeps.services.domain.ELicenseNumberType;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ElectronicJointSignatureLicenseDto {

	private String number;

	private ELicenseNumberType type;

}
