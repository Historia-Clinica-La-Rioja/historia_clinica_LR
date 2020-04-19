package net.pladema.patient.controller.mocks;

import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.mock.MocksPerson;


public class MocksPatient {


    public static BasicPatientDto mockBasicPatientDto(int i) {
        BasicPatientDto result = new BasicPatientDto();
        result.setId(i);
        result.setPerson(MocksPerson.mockBasicPersonDto(i));
        return result;
    }


}
