package net.pladema.patient.controller.dto;

import lombok.*;
import net.pladema.person.controller.dto.BasicDataPersonDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BasicPatientDto {

    private Integer id;

    private BasicDataPersonDto person;

}
