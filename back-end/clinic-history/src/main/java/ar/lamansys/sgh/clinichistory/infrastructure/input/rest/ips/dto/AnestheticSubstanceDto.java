package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class AnestheticSubstanceDto extends ClinicalTermDto {

    @NotNull(message = "{value.mandatory}")
    private @Valid NewDosageDto dosage;

    @NotNull(message = "{value.mandatory}")
    private Short viaId;

    @Nullable
    private String viaNote;
}
