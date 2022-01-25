package net.pladema.person.controller.mapper;

import net.pladema.person.controller.dto.PersonOccupationDto;
import net.pladema.person.service.domain.PersonOccupationBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface OccupationMapper {

    @Named("fromOccupationBo")
    PersonOccupationDto fromOccupationBo(PersonOccupationBo personOccupationBo);

    @Named("fromOccupationBoList")
    @IterableMapping(qualifiedByName = "fromOccupationBo")
    List<PersonOccupationDto> fromOccupationBoList(List<PersonOccupationBo> personOccupationBoList);
}
