package net.pladema.patient.controller.mocks;

import net.pladema.patient.controller.dto.AppointmentPatientDto;
import net.pladema.patient.controller.dto.BMPatientDto;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.person.controller.mock.MocksPerson;
import net.pladema.staff.controller.dto.BasicPersonalDataDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class MocksPatient {


    private MocksPatient(){
        super();
    }

    public static BasicPatientDto mockBasicPatientDto(int i) {
        BasicPatientDto result = new BasicPatientDto();
        result.setId(i);
        result.setPerson(MocksPerson.mockBasicPersonDto(i));
        return result;
    }

    public static List<BMPatientDto> mockBasicListedPatientsList(){
        List<BMPatientDto> result = new ArrayList<>();
        BMPatientDto patient;
        for(int id = 1; id < 11 ; id++){
            patient = mockBasicListedPatient(id);
            result.add(patient);
        }
        return result;
    }

    public static BMPatientDto mockBasicListedPatient(int id) {
        BMPatientDto result = new BMPatientDto();
        result.setId(id);
        result.setIdentificationTypeId((short) 1);
        result.setIdentificationNumber("12345678");
        result.setFirstName("Nombre " + id);
        result.setLastName("Apellido " +id);
        result.setBirthDate(LocalDateTime.now());
        result.setGenderId((short) 1);
        return result;
    }

    public static AppointmentPatientDto mockAppointmentPatientDto(Integer id) {
        AppointmentPatientDto result =
                new AppointmentPatientDto(9,
                        new BasicPersonalDataDto("Maria", "Gonzalez", "12345678")
                        , "OSDE",  "3213211");
        return result;
    }
}
