package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HCEErrorProblemDto {

    @NotNull(message = "{value.mandatory}")
    private DateTimeDto timePerformed;

    @NotNull(message = "{value.mandatory}")
    private String reason;

    @NotNull(message = "{value.mandatory}")
    private String observations;
}
