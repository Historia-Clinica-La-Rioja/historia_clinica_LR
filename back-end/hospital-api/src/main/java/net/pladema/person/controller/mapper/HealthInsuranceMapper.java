package net.pladema.person.controller.mapper;

import net.pladema.person.controller.dto.HealthInsuranceDto;
import net.pladema.person.repository.entity.HealthInsurance;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface HealthInsuranceMapper {

    @Named("fromHealthInsurance")
    HealthInsuranceDto fromHealthInsurance(HealthInsurance healthInsurance);
}
