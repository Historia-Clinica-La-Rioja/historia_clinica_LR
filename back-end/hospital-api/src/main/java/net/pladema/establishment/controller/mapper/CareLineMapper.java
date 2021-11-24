package net.pladema.establishment.controller.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.CareLineDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.establishment.service.domain.CareLineBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface CareLineMapper {

    @Named("toCareLineDto")
    CareLineDto toCareLineDto(CareLineBo careLineBo);

    @Named("toListCareLineDto")
    @IterableMapping(qualifiedByName = "toCareLineDto")
    List<CareLineDto> toListCareLineDto(List<CareLineBo> careLineBoList);

}