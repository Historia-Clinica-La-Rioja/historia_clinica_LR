package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class AllergyConditionBo extends HealthConditionBo {

    private String categoryId;

    private String severity;

    private LocalDate date;
}
