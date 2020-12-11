package net.pladema.clinichistory.requests.medicalrequests.controller.dto;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.sgx.dates.controller.dto.DateDto;

import javax.validation.Valid;

@Getter
@Setter
@AllArgsConstructor
public class MedicalRequestDto {

    @Nullable
    private String observations;

    @Nullable
    private Integer statusId;

    @NotNull
    private SnomedDto healthConditionSnomed;

    @Valid
    private DateDto startDate;



}
