package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.GlobalCoordinatesDto;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface GlobalCoordinatesMapper {

	@Named("fromGlobalCoordinatesDto")
	GlobalCoordinatesBo fromGlobalCoordinatesDto(GlobalCoordinatesDto globalCoordinatesDto);

	@Named("toGlobalCoordinatesDto")
	GlobalCoordinatesDto toGlobalCoordinatesDto(GlobalCoordinatesBo globalCoordinatesBo);

	@IterableMapping(qualifiedByName = "toGlobalCoordinatesDto")
	@Named("toGlobalCoordinatesDtoList")
    List<GlobalCoordinatesDto> toGlobalCoordinatesDtoList(List<GlobalCoordinatesBo> resultBo);

}
