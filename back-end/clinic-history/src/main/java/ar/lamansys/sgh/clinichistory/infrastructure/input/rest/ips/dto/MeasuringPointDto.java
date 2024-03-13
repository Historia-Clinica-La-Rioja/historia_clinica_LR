package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MeasuringPointDto {

    @NotNull
    private DateDto date;

    @NotNull
    private TimeDto time;

    @Nullable
    private Integer bloodPressureMin;

    @Nullable
    private Integer bloodPressureMax;

    @Nullable
    private Integer bloodPulse;

    @Nullable
    private Integer o2Saturation;

    @Nullable
    private Integer co2EndTidal;
}
