package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ProblemBo extends HealthConditionBo {

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private boolean chronic = false;
}
