package net.pladema.clinichistory.requests.medicalrequests.controller.dto;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewMedicalRequestDto {

    @NotNull
    private Integer healthConditionId;

    @Nullable
    private String observations;
}
