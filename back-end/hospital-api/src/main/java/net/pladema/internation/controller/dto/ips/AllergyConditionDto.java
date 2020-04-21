package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AllergyConditionDto extends HealthConditionDto {

    private String categoryId;

    private String severity;

    private String date;
}
