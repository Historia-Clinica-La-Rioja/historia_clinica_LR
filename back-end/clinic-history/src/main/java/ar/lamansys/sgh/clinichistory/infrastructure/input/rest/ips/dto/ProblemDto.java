package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ProblemDto extends HealthConditionDto {

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "{value.mandatory}")
    private String severity;

    private boolean chronic = false;
}
