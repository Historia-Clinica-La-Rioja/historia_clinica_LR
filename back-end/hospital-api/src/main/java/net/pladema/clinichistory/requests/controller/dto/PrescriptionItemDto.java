package net.pladema.clinichistory.requests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.NewDosageDto;

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

    @NotNull(message = "{value.mandatory}")
    private Integer healthConditionId;

    @Nullable
    private String observations;

    @Valid
    @Nullable
    private NewDosageDto dosage;

}
