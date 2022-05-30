package net.pladema.sgx.healthinsurance.controller.mapper;

import net.pladema.patient.controller.dto.PrivateHealthInsuranceDto;
import net.pladema.patient.controller.dto.MedicalCoveragePlanDto;
import net.pladema.patient.service.domain.MedicalCoveragePlanBo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Collection;

@Mapper
public interface PrivateHealthInsuranceMapper {

    @Named("toPrivateHealthInsuranceDtoList")
    Collection<PrivateHealthInsuranceDto> toPrivateHealthInsuranceDtoList(Collection<PrivateHealthInsuranceBo> data);

    @Named("toMedicalCoveragePlanDtoList")
    Collection<MedicalCoveragePlanDto> toMedicalCoveragePlanDtoList(Collection<MedicalCoveragePlanBo> data);

    @Named("toMedicalCoveragePlanDto")
	MedicalCoveragePlanDto toMedicalCoveragePlanDto(MedicalCoveragePlanBo data);


    PrivateHealthInsuranceDto toPrivateHealthInsuranceDto(PrivateHealthInsuranceBo data);
}
