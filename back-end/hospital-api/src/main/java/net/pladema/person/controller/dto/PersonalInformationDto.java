package net.pladema.person.controller.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.address.controller.dto.AddressDto;

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

	private String phonePrefix;

    private String phoneNumber;

    private IdentificationTypeDto identificationType;

    private AddressDto address;


}
