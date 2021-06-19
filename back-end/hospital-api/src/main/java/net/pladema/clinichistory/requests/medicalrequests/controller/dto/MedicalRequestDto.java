package net.pladema.clinichistory.requests.medicalrequests.controller.dto;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;

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
