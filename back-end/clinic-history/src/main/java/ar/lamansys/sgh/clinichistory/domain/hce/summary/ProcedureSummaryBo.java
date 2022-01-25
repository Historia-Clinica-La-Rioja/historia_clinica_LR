package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProcedureSummaryBo extends ClinicalTerm {

    private LocalDate performedDate;

    private Integer consultationId;

    public ProcedureSummaryBo(Integer id, SnomedBo snomed, LocalDate performedDate, Integer consultationId) {
        this.setId(id);
        this.setSnomed(snomed);
        this.performedDate = performedDate;
        this.consultationId = consultationId;
    }
}
