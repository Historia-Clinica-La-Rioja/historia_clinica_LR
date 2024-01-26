package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnestheticSubstanceDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface AnestheticSubstanceMapper {

    @Named(value = "toAnestheticSubstanceBo")
    @Mapping(target = "dosage.startDate", source = "dosage.startDateTime")
    AnestheticSubstanceBo toAnestheticSubstanceBo(AnestheticSubstanceDto AnestheticSubstanceDto);

    @Named(value = "toAnestheticSubstanceDto")
    @Mapping(target = "dosage.startDateTime", source = "dosage.startDate")
    AnestheticSubstanceDto toAnestheticSubstanceDto(AnestheticSubstanceBo AnestheticSubstanceBo);

    @Named("toListAnestheticSubstanceBo")
    @IterableMapping(qualifiedByName = "toAnestheticSubstanceBo")
    List<AnestheticSubstanceBo> toListAnestheticSubstanceBo(List<AnestheticSubstanceDto> AnestheticSubstanceDtos);

    @Named("toListAnestheticSubstanceDto")
    @IterableMapping(qualifiedByName = "toAnestheticSubstanceDto")
    List<AnestheticSubstanceDto> toListAnestheticSubstanceDto(List<AnestheticSubstanceBo> AnestheticSubstanceBos);

}
