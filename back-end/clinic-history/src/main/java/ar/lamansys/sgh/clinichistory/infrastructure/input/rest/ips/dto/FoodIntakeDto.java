package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FoodIntakeDto {

    @NotNull(message = "{value.mandatory}")
    private TimeDto clockTime;

    @Nullable
    @JsonIgnore
    private LocalTime performedTime;
}
