package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AllergyCondition extends HealthConditionBo {

    private String categoryId;

    private String severity;

    private String date;
}
