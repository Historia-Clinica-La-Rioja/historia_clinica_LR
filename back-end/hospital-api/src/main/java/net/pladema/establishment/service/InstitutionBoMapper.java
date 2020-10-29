package net.pladema.establishment.service;

import net.pladema.establishment.repository.entity.Institution;
import net.pladema.establishment.service.domain.InstitutionBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface InstitutionBoMapper {

    @Named("toInstitutionBo")
    InstitutionBo toInstitutionBo(Institution institution);
}
