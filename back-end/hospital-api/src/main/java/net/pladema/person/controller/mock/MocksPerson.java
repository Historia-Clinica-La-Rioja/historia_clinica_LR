package net.pladema.person.controller.mock;


import net.pladema.address.controller.mocks.MocksAddress;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.dto.HealthInsuranceDto;
import net.pladema.person.controller.dto.IdentificationTypeDto;
import net.pladema.person.controller.dto.PersonalInformationDto;

import java.time.LocalDate;

public class MocksPerson {


    public static BasicDataPersonDto mockBasicPersonDto(int i) {
        BasicDataPersonDto result = new BasicDataPersonDto();
        result.setId(i);
        result.setFirstName("Nombre " + i);
        result.setLastName("Apellido " +i);
        result.setGenderId((short)1);
        result.setAge((short)50);
        return result;
    }

    public static PersonalInformationDto mockPersonalInformation(Integer personId) {
        PersonalInformationDto result = new PersonalInformationDto();
        result.setId(personId);
        result.setBirthDate(LocalDate.now());
        result.setCuil("20-12345678-2");
        result.setEmail("email@example.com");
        result.setPhoneNumber("01165489565");
        result.setIdentificationNumber("12345678");
        result.setAddress(MocksAddress.mockAddressDto(personId));
        result.setHealthInsurance(mockHealthInsuranceDto(personId));
        result.setIdentificationType(mockIdentificationTypeDto(personId));
        return result;
    }

    private static HealthInsuranceDto mockHealthInsuranceDto(Integer id){
        HealthInsuranceDto result = new HealthInsuranceDto();
        result.setId(Short.valueOf(id + ""));
        result.setAcronym("OSDE");
        result.setRnos("0123456");
        return result;
    }

    private static IdentificationTypeDto mockIdentificationTypeDto(Integer id){
        IdentificationTypeDto result = new IdentificationTypeDto();
        result.setId(Short.valueOf(id + ""));
        result.setDescription("DNI");
        return result;
    }
}
