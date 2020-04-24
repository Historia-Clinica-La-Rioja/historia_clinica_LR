package net.pladema.person.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HealthInsuranceDto {

    private Short id;

    private String rnos;

    private String acronym;
}
