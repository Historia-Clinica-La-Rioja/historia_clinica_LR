package net.pladema.clinichistory.requests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
public class PrescriptionItemDto implements Serializable {

    @NotNull
    private SnomedDto snomed;

    @NotNull
    private Integer healthConditionId;

    @Nullable
    private String observations;

    @Valid
    @Nullable
    private NewDosageDto dosage;
}
