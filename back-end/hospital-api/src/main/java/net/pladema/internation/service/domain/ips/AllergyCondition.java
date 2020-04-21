package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AllergyCondition extends HealthCondition {

    private String categoryId;

    private String severity;

    private String date;
}
