package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticTechniqueBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnestheticTechniqueDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface AnestheticTechniqueMapper {
    
    @Named(value = "toAnestheticTechniqueBo")
    AnestheticTechniqueBo toAnestheticTechniqueBo(AnestheticTechniqueDto anestheticTechniqueDto);

    @Named(value = "toAnestheticTechniqueDto")
    AnestheticTechniqueDto toAnestheticTechniqueDto(AnestheticTechniqueBo anestheticTechniqueBo);

    @Named("toListAnestheticTechniqueBo")
    @IterableMapping(qualifiedByName = "toAnestheticTechniqueBo")
    List<AnestheticTechniqueBo> toListAnestheticTechniqueBo(List<AnestheticTechniqueDto> anestheticTechniqueDtos);

    @Named("toListAnestheticTechniqueDto")
    @IterableMapping(qualifiedByName = "toAnestheticTechniqueDto")
    List<AnestheticTechniqueDto> toListAnestheticTechniqueDto(List<AnestheticTechniqueBo> anestheticTechniqueBos);
}
