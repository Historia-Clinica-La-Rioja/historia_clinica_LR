package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

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
public class ProcedureDescriptionDto {

    @Nullable
    private String note;

    @NotNull
    @Min(1)
    @Max(5)
    private Short asa;
}
