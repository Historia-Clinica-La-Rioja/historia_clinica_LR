package net.pladema.person.controller.mapper;

import net.pladema.person.controller.dto.SelfPerceivedGenderDto;
import net.pladema.person.repository.entity.SelfPerceivedGender;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface SelfPerceivedGenderMapper {

	@Named("fromSelfPerceivedGender")
	SelfPerceivedGenderDto fromSelfPerceivedGender(SelfPerceivedGender selfPerceivedGender);

	List<SelfPerceivedGenderDto> fromSelfPerceivedGenderList(List<SelfPerceivedGender> genders);
}
