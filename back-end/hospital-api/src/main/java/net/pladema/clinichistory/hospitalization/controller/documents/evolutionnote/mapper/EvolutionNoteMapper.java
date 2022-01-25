package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.mapper;

import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.EvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.ResponseEvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.evolutiondiagnosis.EvolutionDiagnosisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnthropometricDataMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.VitalSignMapper;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.evolutiondiagnosis.EvolutionDiagnosisBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {VitalSignMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class, SnomedMapper.class})
public interface EvolutionNoteMapper {

    @Named("fromEvolutionNoteDto")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromVitalSignDto")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataDto")
    EvolutionNoteBo fromEvolutionNoteDto(EvolutionNoteDto evolutionNoteDto);

    @Named("fromEvolutionNote")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataBo")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromVitalSignBo")
    ResponseEvolutionNoteDto fromEvolutionNote(EvolutionNoteBo evolutionNoteBo);

    EvolutionDiagnosisBo fromEvolutionNoteDto(EvolutionDiagnosisDto evolutionDiagnosisDto);

}
