package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;


import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AnestheticHistoryDto {

    @NotNull(message = "${value.mandatory}")
    private Short stateId;

    @Nullable
    private Short zoneId;
}
