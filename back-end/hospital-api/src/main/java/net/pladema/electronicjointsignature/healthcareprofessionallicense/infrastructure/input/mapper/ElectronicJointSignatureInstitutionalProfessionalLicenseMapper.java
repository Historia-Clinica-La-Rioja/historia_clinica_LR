package net.pladema.electronicjointsignature.healthcareprofessionallicense.infrastructure.input.mapper;

import net.pladema.electronicjointsignature.healthcareprofessionallicense.domain.ElectronicJointSignatureInstitutionProfessionalBo;
import net.pladema.electronicjointsignature.healthcareprofessionallicense.infrastructure.input.dto.ElectronicJointSignatureInstitutionProfessionalDto;

import net.pladema.sisa.refeps.services.domain.ELicenseNumberType;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = ELicenseNumberType.class)
public interface ElectronicJointSignatureInstitutionalProfessionalLicenseMapper {

	default ELicenseNumberType parseELicenseNumberType(Short type) {
		if (type != null)
			return ELicenseNumberType.map(type);
		return null;
	}

	@Mapping(target = "license.type", expression = "java(parseELicenseNumberType(electronicJointSignatureLicenseBo.getType()))")
	@Named("toInstitutionProfessionalDto")
	ElectronicJointSignatureInstitutionProfessionalDto toInstitutionProfessionalDto(ElectronicJointSignatureInstitutionProfessionalBo electronicJointSignatureInstitutionProfessionalBo);

	@IterableMapping(qualifiedByName = "toInstitutionProfessionalDto")
	@Named("toInstitutionProfessionalDtoList")
	List<ElectronicJointSignatureInstitutionProfessionalDto> toInstitutionProfessionalDtoList(List<ElectronicJointSignatureInstitutionProfessionalBo> electronicJointSignatureInstitutionProfessionalBos);

}
