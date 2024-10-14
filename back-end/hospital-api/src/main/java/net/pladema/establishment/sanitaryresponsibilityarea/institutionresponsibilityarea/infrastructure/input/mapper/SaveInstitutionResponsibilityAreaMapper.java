package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.input.mapper;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.domain.SaveInstitutionResponsibilityAreaBo;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.input.rest.dto.SaveInstitutionResponsibilityAreaDto;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper.GlobalCoordinatesMapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {GlobalCoordinatesMapper.class})
public interface SaveInstitutionResponsibilityAreaMapper {

	@Mapping(target = "institutionId", expression = "java(institutionId)")
	@Named("toSaveInstitutionResponsibilityAreaBo")
	SaveInstitutionResponsibilityAreaBo toSaveInstitutionResponsibilityAreaBo(SaveInstitutionResponsibilityAreaDto saveInstitutionResponsibilityAreaDto, @Context Integer institutionId);

}
