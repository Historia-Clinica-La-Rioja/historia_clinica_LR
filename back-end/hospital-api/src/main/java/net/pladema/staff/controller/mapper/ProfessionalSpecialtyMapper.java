package net.pladema.staff.controller.mapper;

import net.pladema.staff.controller.dto.ProfessionalSpecialtyDto;
import net.pladema.staff.service.domain.ProfessionalSpecialtyBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ProfessionalSpecialtyMapper {

    @Named("fromListProfessionalSpecialtyBo")
    List<ProfessionalSpecialtyDto> fromListProfessionalSpecialtyBo(List<ProfessionalSpecialtyBo> professions);

}
