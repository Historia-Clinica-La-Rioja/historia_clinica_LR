package net.pladema.establishment.service;

import net.pladema.establishment.controller.dto.SectorDto;
import net.pladema.establishment.service.domain.SectorBO;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface SectorBOMapper {

	@Named("toSectorDto")
	SectorDto toSectorDto(SectorBO sectorBo);
	@Named("toListSectorDto")
	List<SectorDto> toListSectorDto(List<SectorBO> sectorsBo);
}
