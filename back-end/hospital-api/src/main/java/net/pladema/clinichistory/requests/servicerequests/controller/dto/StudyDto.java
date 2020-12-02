package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class StudyDto implements Serializable {

    private SnomedDto snomed;
    private SnomedDto healthConditionSnomed;

    @Nullable
    private String observations;
}
