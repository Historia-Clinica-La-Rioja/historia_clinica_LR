package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@Setter
@ToString
public class CompleteRequestDto {
    @Nullable
    private String observations;

    @Nullable
    private String link;

    @Nullable
    private List<Integer> fileIds;
}
