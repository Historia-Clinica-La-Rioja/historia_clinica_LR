package net.pladema.internation.controller.documents.evolutionnote.mapper;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.documents.evolutionnote.dto.EvolutionNoteDto;
import net.pladema.internation.controller.documents.evolutionnote.dto.ResponseEvolutionNoteDto;
import net.pladema.internation.controller.ips.mapper.AnthropometricDataMapper;
import net.pladema.internation.controller.ips.mapper.VitalSignMapper;
import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNoteBo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {VitalSignMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class})
public interface EvolutionNoteMapper {

    @Named("fromEvolutionNoteDto")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromAnthropometricDataDto")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromListAnthropometricDataDto")
    EvolutionNoteBo fromEvolutionNoteDto(EvolutionNoteDto evolutionNoteDto);

    @Named("fromEvolutionNote")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataBo")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromVitalSignBo")
    ResponseEvolutionNoteDto fromEvolutionNote(EvolutionNoteBo evolutionNoteBo);
}
