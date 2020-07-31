package net.pladema.medicalconsultation.appointment.controller.mock;

import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentListDto;
import net.pladema.patient.controller.dto.AppointmentPatientDto;
import net.pladema.staff.controller.dto.BasicPersonalDataDto;

import java.util.Arrays;
import java.util.List;


public class AppointmentMock {

    private AppointmentMock(){
        super();
    }

    public static List<AppointmentListDto> mockListAppointmentListDto(List<Integer> diaryIds) {
        return Arrays.asList(
                new AppointmentListDto(10,
                        new AppointmentPatientDto(9,
                                new BasicPersonalDataDto("Maria", "Gonzalez", "12345678")
                                , "OSDE",  "3213211"), "2020-07-13", "07:15", false),
                new AppointmentListDto(11,
                        new AppointmentPatientDto(25,
                                new BasicPersonalDataDto("Jorge", "Martines", "12345678")
                                , "OSDE",  "321344"), "2020-07-13", "07:30", false)
        );
    }




}
