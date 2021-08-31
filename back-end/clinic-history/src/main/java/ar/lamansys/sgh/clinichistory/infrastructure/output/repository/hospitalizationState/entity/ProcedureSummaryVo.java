package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProcedureSummaryVo extends ClinicalTerm {

    private LocalDate performedDate;

    private Integer consultationID;

    public ProcedureSummaryVo(Integer id, Snomed snomed, LocalDate performedDate, Integer consultationID){
        this.setSnomed(new SnomedBo(snomed));
        this.performedDate = performedDate;
        this.consultationID = consultationID;
    }
}
