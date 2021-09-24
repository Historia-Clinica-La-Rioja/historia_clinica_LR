package ar.lamansys.sgh.publicapi.infrastructure.input.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class AttentionInfoDto implements Serializable {

    Integer id;
    DateDto attentionDate; //LocalDate
    ClinicalSpecialityDto speciality;
    PersonInfoDto patient;
    CoverageActivityInfoDto coverage;
    String scope; // Enum [A, I, E]
    InternmentDto internmentInfo;
    ProfessionalDto responsibleDoctor;
}
