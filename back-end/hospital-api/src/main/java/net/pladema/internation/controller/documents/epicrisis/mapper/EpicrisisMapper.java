package net.pladema.internation.controller.documents.epicrisis.mapper;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.documents.epicrisis.dto.EpicrisisDto;
import net.pladema.internation.controller.documents.epicrisis.dto.EpicrisisGeneralStateDto;
import net.pladema.internation.controller.documents.epicrisis.dto.NewEpicrisisDto;
import net.pladema.internation.controller.documents.epicrisis.dto.ResponseEpicrisisDto;
import net.pladema.internation.controller.ips.mapper.AnthropometricDataMapper;
import net.pladema.internation.controller.ips.mapper.VitalSignMapper;
import net.pladema.internation.service.documents.epicrisis.domain.Epicrisis;
import net.pladema.internation.service.internment.domain.InternmentGeneralState;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {VitalSignMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class})
public interface EpicrisisMapper {

    @Named("fromEpicrisisDto")
    Epicrisis fromEpicrisisDto(EpicrisisDto epicrisisDto);

    @Named("fromNewEpicrisisDto")
    Epicrisis fromNewEpicrisisDto(NewEpicrisisDto newEpicrisisDto);

    @Named("fromEpicrisis")
    ResponseEpicrisisDto fromEpicrisis(Epicrisis epicrisis);

    EpicrisisGeneralStateDto toEpicrisisGeneralStateDto(InternmentGeneralState interment);
}
