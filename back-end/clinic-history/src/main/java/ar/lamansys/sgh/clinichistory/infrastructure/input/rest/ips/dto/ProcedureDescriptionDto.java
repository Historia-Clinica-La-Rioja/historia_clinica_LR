package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
}