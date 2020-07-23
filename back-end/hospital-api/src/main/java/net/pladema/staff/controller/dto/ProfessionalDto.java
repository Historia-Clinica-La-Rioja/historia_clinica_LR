package net.pladema.staff.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProfessionalDto extends BasicPersonalDataDto {

    private Integer id;

    private String licenceNumber;
}
