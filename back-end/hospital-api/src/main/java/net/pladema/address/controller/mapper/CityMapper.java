package net.pladema.address.controller.mapper;

import net.pladema.address.controller.dto.CityDto;
import net.pladema.address.repository.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface CityMapper {

    @Named("fromCity")
    public CityDto fromCity(City city);
}
