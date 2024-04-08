package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import javax.annotation.Nullable;
import javax.validation.Valid;
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

    private static final long serialVersionUID = -6105375851400552126L;

    @Nullable
    private @Valid NewDosageDto dosage;

    @Nullable
    private Short viaId;

    @Nullable
    private String viaNote;
}
