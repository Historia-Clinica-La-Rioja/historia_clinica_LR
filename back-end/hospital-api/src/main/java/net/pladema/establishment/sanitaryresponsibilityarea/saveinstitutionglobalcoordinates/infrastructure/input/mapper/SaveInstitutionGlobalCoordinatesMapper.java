package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.infrastructure.input.mapper;

import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.domain.SaveInstitutionGlobalCoordinatesBo;

import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.infrastructure.input.rest.dto.SaveInstitutionGlobalCoordinatesDto;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface SaveInstitutionGlobalCoordinatesMapper {

	@Mapping(target = "institutionId", expression = "java(institutionId)")
	@Named("toSaveInstitutionGlobalCoordinatesBo")
	SaveInstitutionGlobalCoordinatesBo toSaveInstitutionGlobalCoordinatesBo(SaveInstitutionGlobalCoordinatesDto saveInstitutionGlobalCoordinatesDto, @Context Integer institutionId);

}
