package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @Min(20)
    @Max(140)
    private Integer bloodPressureMin;

    @Nullable
    @Min(20)
    @Max(140)
    private Integer bloodPressureMax;

    @Nullable
    @Min(70)
    @Max(100)
    private Integer bloodPulse;

    @Nullable
    @Min(0)
    @Max(100)
    private Integer o2Saturation;

    @Nullable
    @Min(0)
    @Max(240)
    private Integer co2EndTidal;
}
