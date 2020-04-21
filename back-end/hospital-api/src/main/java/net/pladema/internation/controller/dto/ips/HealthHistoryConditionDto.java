package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class HealthHistoryConditionDto extends HealthConditionDto {

    private String date;

    private String note;
}
