package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceSummaryVo {

    private Integer id;

    private String careLine;

    private String clinicalSpecialty;

    private String note;

}
