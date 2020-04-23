package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HealthHistoryConditionDto extends HealthConditionDto {

    private String date;

    private String note;
}
