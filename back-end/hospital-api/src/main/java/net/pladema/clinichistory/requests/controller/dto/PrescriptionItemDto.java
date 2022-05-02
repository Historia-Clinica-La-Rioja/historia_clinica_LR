package net.pladema.clinichistory.requests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionItemDto implements Serializable {

    @Valid
    @NotNull(message = "{value.mandatory}")
    private SnomedDto snomed;

    private Integer healthConditionId;

    @Nullable
    private String observations;

    @Valid
    @Nullable
    private NewDosageDto dosage;

    @Nullable
    private String categoryId;

}
