package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.mapper;

import net.pladema.clinichistory.documents.service.generalstate.EncounterGeneralState;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisDto;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisGeneralStateDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.ResponseEpicrisisDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.AnthropometricDataMapper;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.VitalSignMapper;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {VitalSignMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class})
public interface EpicrisisMapper {

    @Named("fromEpicrisisDto")
    EpicrisisBo fromEpicrisisDto(EpicrisisDto epicrisisDto);

    @Named("fromEpicrisis")
    ResponseEpicrisisDto fromEpicrisis(EpicrisisBo epicrisisBo);

    EpicrisisGeneralStateDto toEpicrisisGeneralStateDto(EncounterGeneralState interment);
}
