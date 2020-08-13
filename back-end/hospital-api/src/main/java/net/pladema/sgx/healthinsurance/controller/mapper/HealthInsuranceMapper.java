package net.pladema.sgx.healthinsurance.controller.mapper;

import net.pladema.renaper.controller.dto.MedicalCoverageDto;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Collection;

@Mapper
public interface HealthInsuranceMapper {

    @Named("toMedicalCoverageDtoList")
    Collection<MedicalCoverageDto> toMedicalCoverageDtoList(Collection<PersonMedicalCoverageBo> data);
}
