package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class AttentionInfoDto implements Serializable {

    private Integer id;
    private DateDto attentionDate;
    private ClinicalSpecialityDto speciality;
    private PersonInfoDto patient;
    private CoverageActivityInfoDto coverage;
    private String scope;
    private InternmentDto internmentInfo;
    private ProfessionalDto responsibleDoctor;
}
