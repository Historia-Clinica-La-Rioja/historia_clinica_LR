package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.person.controller.dto.BasicPersonalDataDto;
@AllArgsConstructor
@Setter
@Getter
public class ReducedPatientDto {

    private BasicPersonalDataDto personalDataDto;

    private Short patientTypeId;

}
