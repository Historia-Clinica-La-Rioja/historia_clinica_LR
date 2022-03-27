package ar.lamansys.nursing.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Validated
public class NursingConsultationDto implements Serializable {

    @Valid
    @NotNull(message = "{value.mandatory}")
    private Integer clinicalSpecialtyId;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private NursingProblemDto problem;

    @Valid
    @Nullable
    private NursingAnthropometricDataDto anthropometricData;

    @Valid
    @Nullable
    private NursingRiskFactorDto riskFactors;

    @Valid
    @Nullable
    private List<@Valid NursingProcedureDto> procedures = new ArrayList<>();

    @Nullable
    private String evolutionNote;

}
