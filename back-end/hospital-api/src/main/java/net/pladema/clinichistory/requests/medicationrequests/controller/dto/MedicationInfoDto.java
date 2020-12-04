package net.pladema.clinichistory.requests.medicationrequests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.sgx.dates.controller.dto.DateDto;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class MedicationInfoDto implements Serializable {

    @NotNull
    private SnomedDto snomed;

    @Nullable
    private String statusId;

    @NotNull
    private SnomedDto healthCondition;

    @Nullable
    private boolean expired;

    @Valid
    @NotNull
    private DosageDto dosage;

    @Valid
    private DateDto startDate;

    @Nullable
    private boolean chronic;

    @Nullable
    private String observations;

    @Nullable
    private Integer medicationRequestId;
}
