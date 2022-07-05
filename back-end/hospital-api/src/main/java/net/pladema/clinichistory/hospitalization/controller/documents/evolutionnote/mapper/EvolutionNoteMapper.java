package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.EvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.ResponseEvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.evolutiondiagnosis.EvolutionDiagnosisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnthropometricDataMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {RiskFactorMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class, SnomedMapper.class})
public interface EvolutionNoteMapper {

    @Named("fromEvolutionNoteDto")
    @Mapping(target = "riskFactors", source = "riskFactors", qualifiedByName = "fromRiskFactorDto")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataDto")
    EvolutionNoteBo fromEvolutionNoteDto(EvolutionNoteDto evolutionNoteDto);

    @Named("fromEvolutionNote")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataBo")
    @Mapping(target = "riskFactors", source = "riskFactors", qualifiedByName = "fromRiskFactorBo")
    ResponseEvolutionNoteDto fromEvolutionNote(EvolutionNoteBo evolutionNoteBo);

	EvolutionNoteBo fromEvolutionDiagnosisDto(EvolutionDiagnosisDto evolutionDiagnosisDto);

}
