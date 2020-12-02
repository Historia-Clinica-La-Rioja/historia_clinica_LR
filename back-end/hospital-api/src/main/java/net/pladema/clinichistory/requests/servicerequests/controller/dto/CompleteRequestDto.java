package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
public class CompleteRequestDto {
    @Nullable
    private String observations;

    @Nullable
    private String link;
}
