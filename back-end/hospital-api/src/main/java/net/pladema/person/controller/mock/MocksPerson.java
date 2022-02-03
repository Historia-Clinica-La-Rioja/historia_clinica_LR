package net.pladema.person.controller.mock;


import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import net.pladema.address.controller.mocks.MocksAddress;
import net.pladema.person.controller.dto.*;

import java.time.LocalDate;

public class MocksPerson {

    private MocksPerson() {
        super();
    }

    public static PersonalInformationDto mockPersonalInformation(int id) {
        PersonalInformationDto result = new PersonalInformationDto();
        result.setId(id);
        result.setBirthDate(LocalDate.now());
        result.setCuil("20-12345678-2");
        result.setEmail("email@example.com");
        result.setPhoneNumber("01165489565");
        result.setIdentificationNumber("12345678");
        result.setAddress(MocksAddress.mockAddressDto(id));
        result.setIdentificationType(mockIdentificationTypeDto(id));
        return result;
    }

    private static IdentificationTypeDto mockIdentificationTypeDto(int id){
        IdentificationTypeDto result = new IdentificationTypeDto();
        result.setId((short)id);
        result.setDescription("DNI");
        return result;
    }

    private static GenderDto mockGenderDto(int id){
        GenderDto result = new GenderDto();
        result.setId((short)id);
        result.setDescription("Masculino");
        return result;
    }
}
