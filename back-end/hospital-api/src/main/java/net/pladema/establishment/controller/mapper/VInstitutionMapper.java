package net.pladema.establishment.controller.mapper;

import net.pladema.establishment.controller.dto.VInstitutionDto;
import net.pladema.establishment.service.domain.VInstitutionBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface VInstitutionMapper {

    @Named("toVInstitutionDto")
    VInstitutionDto toVInstitutionDto(VInstitutionBo generalState);
}
