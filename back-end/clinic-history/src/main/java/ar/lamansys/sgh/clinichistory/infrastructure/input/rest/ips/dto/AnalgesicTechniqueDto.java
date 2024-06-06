package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AnalgesicTechniqueDto extends AnestheticSubstanceDto {

    @Nullable
    private String injectionNote;

    @Nullable
    private Boolean catheter;

    @Nullable
    private String catheterNote;
}
