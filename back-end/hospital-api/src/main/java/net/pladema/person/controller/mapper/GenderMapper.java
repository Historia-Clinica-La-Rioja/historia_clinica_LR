package net.pladema.person.controller.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import net.pladema.person.controller.dto.SelfPerceivedGenderDto;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.SelfPerceivedGender;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface GenderMapper {

    @Named("fromGender")
	GenderDto fromGender(Gender gender);

    List<GenderDto> fromGenderList(List<Gender> genders);

    @Named("fromSelfPerceivedGender")
    SelfPerceivedGenderDto fromSelfPerceivedGender(SelfPerceivedGender selfPerceivedGender);

    List<SelfPerceivedGenderDto> fromSelfPerceivedGenderList(List<SelfPerceivedGender> genders);
}
