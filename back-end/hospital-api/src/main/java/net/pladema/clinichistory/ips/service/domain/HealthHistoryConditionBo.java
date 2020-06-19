package net.pladema.clinichistory.ips.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class HealthHistoryConditionBo extends HealthConditionBo {

    private LocalDate date;

    private String note;
}
