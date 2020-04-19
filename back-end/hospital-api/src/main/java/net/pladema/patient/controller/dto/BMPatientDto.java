package net.pladema.patient.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.controller.dto.APersonDto;

@Getter
@Setter
@NoArgsConstructor
public class BMPatientDto extends APersonDto {

    private Integer id;
}
