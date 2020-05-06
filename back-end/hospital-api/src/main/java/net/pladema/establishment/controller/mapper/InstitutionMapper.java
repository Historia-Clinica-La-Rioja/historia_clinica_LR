package net.pladema.establishment.controller.mapper;


import net.pladema.establishment.controller.dto.InstitutionDto;
import net.pladema.establishment.repository.entity.Institution;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface InstitutionMapper {

	@Named("toInstitutionDto")
	InstitutionDto toInstitutionDto(Institution institution);

	@Named("toListInstitutionDto")
	@IterableMapping(qualifiedByName = "toInstitutionDto")
	List<InstitutionDto> toListInstitutionDto(List<Institution> roomList);

}
