package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @Nullable
    @JsonIgnore
    private LocalDate localDate;

    @Nullable
    @JsonIgnore
    private LocalTime localTime;

    public boolean hasPointValues() {
        return bloodPressureMin != null
                || bloodPressureMax != null
                || bloodPulse != null
                || o2Saturation != null
                || co2EndTidal != null;
    }
}
