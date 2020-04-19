package net.pladema.person.controller.mapper;

import net.pladema.address.controller.dto.CityDto;
import net.pladema.address.repository.entity.City;
import net.pladema.person.controller.dto.HealthInsuranceDto;
import net.pladema.person.repository.entity.HealthInsurance;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface HealthInsuranceMapper {

    @Named("fromHealthInsurance")
    public HealthInsuranceDto fromHealthInsurance(HealthInsurance healthInsurance);
}
