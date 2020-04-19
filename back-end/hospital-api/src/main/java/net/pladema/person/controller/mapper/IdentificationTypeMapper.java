package net.pladema.person.controller.mapper;

import net.pladema.person.controller.dto.HealthInsuranceDto;
import net.pladema.person.controller.dto.IdentificationTypeDto;
import net.pladema.person.repository.entity.HealthInsurance;
import net.pladema.person.repository.entity.IdentificationType;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface IdentificationTypeMapper {

    @Named("fromIdentificationType")
    public IdentificationTypeDto fromIdentificationType(IdentificationType identificationType);
}
