package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReferenceSummaryVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceSummaryBo {

    private Integer id;

    private String careLine;

    private String clinicalSpecialty;

    private String note;
    
    private List<ReferenceCounterReferenceFileBo> files;

    public ReferenceSummaryBo(Integer id, String careLine, String clinicalSpecialty, String note) {
        this.id = id;
        this.careLine = careLine;
        this.clinicalSpecialty = clinicalSpecialty;
        this.note = note;
    }

}
