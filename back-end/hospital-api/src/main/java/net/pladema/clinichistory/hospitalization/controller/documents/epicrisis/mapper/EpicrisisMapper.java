package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.mapper;

import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.HospitalizationGeneralState;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisGeneralStateDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.ResponseEpicrisisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnthropometricDataMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {RiskFactorMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class, SnomedMapper.class})
public interface EpicrisisMapper {

    @Named("fromEpicrisisDto")
    EpicrisisBo fromEpicrisisDto(EpicrisisDto epicrisisDto);

    @Named("fromEpicrisis")
    ResponseEpicrisisDto fromEpicrisis(EpicrisisBo epicrisisBo);

    EpicrisisGeneralStateDto toEpicrisisGeneralStateDto(HospitalizationGeneralState interment);
}
