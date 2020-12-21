package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@ToString
public class NewServiceRequestListDto implements Serializable {
    private Integer medicalCoverageId;
    private List<StudyDto> studiesDto;
}
