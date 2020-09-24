package net.pladema.medicalconsultation.appointment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import net.pladema.person.controller.dto.BasicPersonalDataDto;


@Value
@Builder
@ToString
@AllArgsConstructor
public class AppointmentBasicPatientDto {

    private final Integer id;

    private final BasicPersonalDataDto person;

    private final Short typeId;
}
