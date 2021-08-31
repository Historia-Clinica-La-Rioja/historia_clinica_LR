package net.pladema.staff.controller.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import net.pladema.staff.controller.dto.ProfessionalsByClinicalSpecialtyDto;
import net.pladema.staff.service.domain.ClinicalSpecialtyBo;
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

    @Named("fromClinicalSpecialtyBo")
    ClinicalSpecialtyDto fromClinicalSpecialtyBo(ClinicalSpecialtyBo specialty);

    @Named("fromListClinicalSpecialtyBo")
    @IterableMapping(qualifiedByName = "fromClinicalSpecialtyBo")
    List<ClinicalSpecialtyDto> fromListClinicalSpecialtyBo(List<ClinicalSpecialtyBo> specialties);
}
