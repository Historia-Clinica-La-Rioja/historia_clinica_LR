package net.pladema.clinichistory.outpatient.infrastructure.input.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ErrorProblemDto extends ProblemInfoDto {

    @NotNull(message = "{value.mandatory}")
    private Integer id;

    @NotNull(message = "{value.mandatory}")
    private Short errorReasonId;

    @NotNull(message = "{value.mandatory}")
    private String errorObservations;
}
