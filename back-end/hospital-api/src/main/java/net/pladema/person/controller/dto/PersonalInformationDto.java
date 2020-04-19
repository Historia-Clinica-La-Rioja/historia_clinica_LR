package net.pladema.person.controller.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.dto.CityDto;
import net.pladema.address.controller.dto.ProvinceDto;
import net.pladema.address.repository.entity.Address;
import net.pladema.address.repository.entity.City;
import net.pladema.address.repository.entity.Province;
import net.pladema.person.repository.entity.HealthInsurance;
import net.pladema.person.repository.entity.IdentificationType;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class PersonalInformationDto {

    private Integer id;

    private String identificationNumber;

    private LocalDate birthDate;

    private String email;

    private String cuil;

    private String phoneNumber;

    private IdentificationTypeDto identificationType;

    private HealthInsuranceDto healthInsurance;

    private AddressDto address;


}
