package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.infrastructure.input.mapper;

import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.domain.SaveInstitutionAddressBo;

import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.infrastructure.input.rest.dto.SaveInstitutionAddressDto;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface SaveInstitutionAddressMapper {

	@Mapping(target = "institutionId", expression = "java(institutionId)")
	@Named("toSaveInstitutionAddressAndGlobalCoordinatesBo")
	SaveInstitutionAddressBo toSaveInstitutionAddressAndGlobalCoordinatesBo(SaveInstitutionAddressDto saveInstitutionAddressDto, @Context Integer institutionId);

}
