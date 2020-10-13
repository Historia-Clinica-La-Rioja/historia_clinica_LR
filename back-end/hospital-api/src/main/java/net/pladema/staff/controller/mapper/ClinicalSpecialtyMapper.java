package net.pladema.staff.controller.mapper;

import net.pladema.staff.controller.dto.ProfessionalsByClinicalSpecialtyDto;
import net.pladema.staff.service.domain.ProfessionalsByClinicalSpecialtyBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ClinicalSpecialtyMapper {

    @Named("fromProfessionalsByClinicalSpecialtyBo")
    ProfessionalsByClinicalSpecialtyDto fromProfessionalsByClinicalSpecialtyBo(ProfessionalsByClinicalSpecialtyBo professionalsByClinicalSpecialtyBo);

    @Named("fromProfessionalsByClinicalSpecialtyBoList")
    @IterableMapping(qualifiedByName = "fromProfessionalsByClinicalSpecialtyBo")
    List<ProfessionalsByClinicalSpecialtyDto> fromProfessionalsByClinicalSpecialtyBoList(List<ProfessionalsByClinicalSpecialtyBo> professionalsByClinicalSpecialtyBos);

}
