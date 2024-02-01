package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.AnalgesicTechniqueBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnalgesicTechniqueDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class, AnestheticSubstanceMapper.class})
public interface AnalgesicTechniqueMapper {

    @Named(value = "toAnalgesicTechniqueBo")
    AnalgesicTechniqueBo toAnalgesicTechniqueBo(AnalgesicTechniqueDto analgesicTechniqueDto);

    @Named(value = "toAnalgesicTechniqueDto")
    AnalgesicTechniqueDto toAnalgesicTechniqueDto(AnalgesicTechniqueBo analgesicTechniqueBo);

    @Named("toListAnalgesicTechniqueBo")
    @IterableMapping(qualifiedByName = "toAnalgesicTechniqueBo")
    List<AnalgesicTechniqueBo> toListAnalgesicTechniqueBo(List<AnalgesicTechniqueDto> analgesicTechniqueDtos);

    @Named("toListAnalgesicTechniqueDto")
    @IterableMapping(qualifiedByName = "toAnalgesicTechniqueDto")
    List<AnalgesicTechniqueDto> toListAnalgesicTechniqueDto(List<AnalgesicTechniqueBo> analgesicTechniqueBos);

}
