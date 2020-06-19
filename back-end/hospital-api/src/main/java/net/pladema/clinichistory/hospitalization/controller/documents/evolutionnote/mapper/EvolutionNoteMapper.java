package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.mapper;

import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.EvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.ResponseEvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.evolutionDiagnosis.EvolutionDiagnosisDto;
import net.pladema.clinichistory.ips.controller.mapper.AnthropometricDataMapper;
import net.pladema.clinichistory.ips.controller.mapper.VitalSignMapper;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.evolutiondiagnosis.EvolutionDiagnosisBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
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

    EvolutionDiagnosisBo fromEvolutionNoteDto(EvolutionDiagnosisDto evolutionDiagnosisDto);

}
