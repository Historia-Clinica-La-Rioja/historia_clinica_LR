package net.pladema.person.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import net.pladema.person.repository.entity.Gender;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface GenderMapper {

    @Named("fromGender")
	GenderDto fromGender(Gender gender);

    List<GenderDto> fromGenderList(List<Gender> genders);

	@Named("fromEGender")
	@Mapping(target = "description", source = "value")
	GenderDto fromEGender(EGender gender);

}
