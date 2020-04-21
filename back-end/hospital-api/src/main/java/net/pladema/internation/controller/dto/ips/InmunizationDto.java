package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InmunizationDto extends ClinicalTermDto {

    private String date;

    private String note;
}
