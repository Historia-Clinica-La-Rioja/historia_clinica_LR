package net.pladema.establishment.controller.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.establishment.repository.entity.Bed;

@Mapper
public interface BedMapper {

    @Named("toBedDto")
    BedDto toBedDto(Bed bed);

    @Named("toListBedDto")
    @IterableMapping(qualifiedByName = "toBedDto")
    List<BedDto> toListBedDto(List<Bed> bedList);

}
