package net.pladema.medicalconsultation.appointment.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.pladema.patient.controller.dto.AppointmentPatientDto;

@Getter
@ToString
@AllArgsConstructor
public class AppointmentListDto {

    private Integer id;

    private AppointmentPatientDto patient;

    private String date;

    private String hour;

    private boolean isOverturn = false;
}
