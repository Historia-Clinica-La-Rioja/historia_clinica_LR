package net.pladema.person.controller.mapper;

import net.pladema.person.controller.dto.GenderDto;
import net.pladema.person.repository.entity.Gender;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface GenderMapper {

    @Named("fromGender")
    GenderDto fromGender(Gender gender);

    List<GenderDto> fromGenderList(List<Gender> genders);
}
