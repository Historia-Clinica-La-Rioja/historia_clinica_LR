package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.service.domain.HealthConditionBo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ProblemBo extends HealthConditionBo {

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean chronic;
}
