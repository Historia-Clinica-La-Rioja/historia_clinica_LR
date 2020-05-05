package net.pladema.establishment.controller.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import net.pladema.establishment.controller.dto.SectorDto;
import net.pladema.establishment.repository.entity.Sector;

@Mapper
public interface SectorMapper {

    @Named("toSectorDto")
    SectorDto toSectorDto(Sector sector);

    @Named("toListSectorDto")
    @IterableMapping(qualifiedByName = "toSectorDto")
    List<SectorDto> toListSectorDto(List<Sector> sectorList);

}
