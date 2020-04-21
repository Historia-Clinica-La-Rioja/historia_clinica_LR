package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HealthHistoryCondition extends HealthCondition {

    private String date;

    private String note;
}
