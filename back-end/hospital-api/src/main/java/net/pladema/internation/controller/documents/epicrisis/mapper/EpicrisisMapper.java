package net.pladema.internation.controller.documents.epicrisis.mapper;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.documents.epicrisis.dto.EpicrisisDto;
import net.pladema.internation.controller.documents.epicrisis.dto.EpicrisisGeneralStateDto;
import net.pladema.internation.controller.documents.epicrisis.dto.ResponseEpicrisisDto;
import net.pladema.internation.controller.ips.mapper.AnthropometricDataMapper;
import net.pladema.internation.controller.ips.mapper.VitalSignMapper;
import net.pladema.internation.service.documents.epicrisis.domain.EpicrisisBo;
import net.pladema.internation.service.internment.domain.InternmentGeneralState;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {VitalSignMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class})
public interface EpicrisisMapper {

    @Named("fromEpicrisisDto")
    EpicrisisBo fromEpicrisisDto(EpicrisisDto epicrisisDto);

    @Named("fromEpicrisis")
    ResponseEpicrisisDto fromEpicrisis(EpicrisisBo epicrisisBo);

    EpicrisisGeneralStateDto toEpicrisisGeneralStateDto(InternmentGeneralState interment);
}
