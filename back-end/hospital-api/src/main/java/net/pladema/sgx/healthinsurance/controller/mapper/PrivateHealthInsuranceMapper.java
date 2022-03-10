package net.pladema.sgx.healthinsurance.controller.mapper;

import net.pladema.patient.controller.dto.PrivateHealthInsuranceDto;
import net.pladema.patient.controller.dto.PrivateHealthInsurancePlanDto;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;
import net.pladema.patient.service.domain.PrivateHealthInsurancePlanBo;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Collection;

@Mapper
public interface PrivateHealthInsuranceMapper {

    @Named("toPrivateHealthInsuranceDtoList")
    Collection<PrivateHealthInsuranceDto> toPrivateHealthInsuranceDtoList(Collection<PrivateHealthInsuranceBo> data);

    @Named("toPrivateHealthInsurancePlanDtoList")
    Collection<PrivateHealthInsurancePlanDto> toPrivateHealthInsurancePlanDtoList(Collection<PrivateHealthInsurancePlanBo> data);

    @Named("toPrivateHealthInsurancePlanDto")
    PrivateHealthInsurancePlanDto toPrivateHealthInsurancePlanDto(PrivateHealthInsurancePlanBo data);


    PrivateHealthInsuranceDto toPrivateHealthInsuranceDto(PrivateHealthInsuranceBo data);
}
