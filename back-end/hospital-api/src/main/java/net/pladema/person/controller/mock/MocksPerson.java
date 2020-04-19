package net.pladema.person.controller.mock;

import net.pladema.internation.controller.dto.*;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.person.controller.dto.BasicDataPersonDto;

import java.util.ArrayList;
import java.util.List;


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
}
