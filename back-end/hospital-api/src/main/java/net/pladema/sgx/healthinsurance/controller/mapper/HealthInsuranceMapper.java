package net.pladema.sgx.healthinsurance.controller.mapper;

import net.pladema.patient.controller.dto.MedicalCoveragePlanDto;
import net.pladema.patient.service.domain.MedicalCoveragePlanBo;
import net.pladema.renaper.controller.dto.MedicalCoverageDto;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Collection;

@Mapper
public interface HealthInsuranceMapper {

    @Named("toMedicalCoverageDtoList")
    Collection<MedicalCoverageDto> toMedicalCoverageDtoList(Collection<PersonMedicalCoverageBo> data);

    MedicalCoverageDto toMedicalCoverageDto (PersonMedicalCoverageBo data);

	@Named("toMedicalCoveragePlanDtoList")
	Collection<MedicalCoveragePlanDto> toMedicalCoveragePlanDtoList(Collection<MedicalCoveragePlanBo> data);

	@Named("toMedicalCoveragePlanDto")
	MedicalCoveragePlanDto toMedicalCoveragePlanDto(MedicalCoveragePlanBo data);
}
