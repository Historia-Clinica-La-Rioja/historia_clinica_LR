package net.pladema.person.controller.mapper;

import net.pladema.person.controller.dto.GenderDto;
import net.pladema.person.controller.dto.IdentificationTypeDto;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.IdentificationType;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface GenderMapper {

    @Named("fromGender")
    public GenderDto fromGender(Gender gender);

    public List<GenderDto> fromGenderList(List<Gender> genders);
}
