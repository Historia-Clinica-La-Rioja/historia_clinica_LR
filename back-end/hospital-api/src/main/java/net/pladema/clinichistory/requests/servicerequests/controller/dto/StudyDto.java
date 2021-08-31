package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class StudyDto implements Serializable {

    private SnomedDto snomed;

    private Integer healthConditionId;

    private String diagosticReportCategoryId;

    @Nullable
    private String observations;
}
