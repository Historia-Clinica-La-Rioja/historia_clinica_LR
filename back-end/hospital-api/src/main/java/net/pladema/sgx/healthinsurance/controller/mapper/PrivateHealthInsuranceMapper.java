package net.pladema.sgx.healthinsurance.controller.mapper;

import net.pladema.patient.controller.dto.PrivateHealthInsuranceDto;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;
import net.pladema.renaper.controller.dto.MedicalCoverageDto;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Collection;

@Mapper
public interface PrivateHealthInsuranceMapper {

    @Named("toPrivateHealthInsuranceDtoList")
    Collection<PrivateHealthInsuranceDto> toPrivateHealthInsuranceDtoList(Collection<PrivateHealthInsuranceBo> data);

    PrivateHealthInsuranceDto toPrivateHealthInsuranceDto(PrivateHealthInsuranceBo data);
}
