package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
public class ProcedureDescriptionDto {

    @Nullable
    private String note;

    @Nullable
    @Min(1)
    @Max(5)
    private Short asa;

    @Nullable
    private Boolean venousAccess;

    @Nullable
    private Boolean nasogastricTube;

    @Nullable
    private Boolean urinaryCatheter;

    @Nullable
    private DateDto anesthesiaStartDate;

    @Nullable
    private TimeDto anesthesiaStartTime;

    @Nullable
    private DateDto anesthesiaEndDate;

    @Nullable
    private TimeDto anesthesiaEndTime;

    @Nullable
    private DateDto surgeryStartDate;

    @Nullable
    private TimeDto surgeryStartTime;

    @Nullable
    private DateDto surgeryEndDate;

    @Nullable
    private TimeDto surgeryEndTime;

    @Nullable
    @JsonIgnore
    private LocalDate anesthesiaStartLocalDate;

    @Nullable
    @JsonIgnore
    private LocalTime anesthesiaStartLocalTime;

    @Nullable
    @JsonIgnore
    private LocalDate anesthesiaEndLocalDate;

    @Nullable
    @JsonIgnore
    private LocalTime anesthesiaEndLocalTime;

    @Nullable
    @JsonIgnore
    private LocalDate surgeryStartLocalDate;

    @Nullable
    @JsonIgnore
    private LocalTime surgeryStartLocalTime;

    @Nullable
    @JsonIgnore
    private LocalDate surgeryEndLocalDate;

    @Nullable
    @JsonIgnore
    private LocalTime surgeryEndLocalTime;

    public boolean hasHistoryStringValues() {
        return (note != null  && !note.isEmpty()) || asa != null;
    }

    public boolean hasIntrasurgicalAnestheticProceduresValues() {
        return venousAccess != null
                || nasogastricTube != null
                || urinaryCatheter != null;
    }

    public boolean hasTimeValues() {
        return anesthesiaStartLocalDate != null
                || anesthesiaStartLocalTime != null
                || anesthesiaEndLocalDate != null
                || anesthesiaEndLocalTime != null
                ||surgeryStartLocalDate != null
                || surgeryStartLocalTime != null
                || surgeryEndLocalDate != null
                || surgeryEndLocalTime != null;

    }

}
