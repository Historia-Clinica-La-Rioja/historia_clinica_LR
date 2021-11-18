package net.pladema.staff.controller.mapper;

import net.pladema.staff.controller.dto.HealthcareProfessionalSpecialtyDto;
import net.pladema.staff.service.domain.HealthcareProfessionalSpecialtyBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface HealthcareProfessionalSpecialtyMapper {

    @Named("fromHealthcareProfessionalSpecialtyBoList")
    List<HealthcareProfessionalSpecialtyDto> fromHealthcareProfessionalSpecialtyBoList(List<HealthcareProfessionalSpecialtyBo> healthcareProfessionalSpecialtiesBo);

}
