package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ImmunizationDto;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ImmunizationMapper {

    @Named("toImmunizationDto")
    ImmunizationDto toImmunizationDto(ImmunizationBo immunizationBo);

    @Named("toImmunizationBo")
    ImmunizationBo toImmunizationBo(ImmunizationDto immunizationDto);

    @Named("toListImmunizationBo")
    @IterableMapping(qualifiedByName = "toImmunizationBo")
    List<ImmunizationBo> toListImmunizationBo(List<ImmunizationDto> immunizationDto);

}
