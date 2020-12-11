package net.pladema.clinichistory.requests.medicalrequests.controller.dto;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

@Getter
@Setter
@AllArgsConstructor
public class NewMedicalRequestDto {

    @NotNull
    private SnomedDto healthConditionSnomed;

    @Nullable
    private String observations;
}
