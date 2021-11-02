package ar.lamansys.sgh.clinichistory.domain.hce;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class HCEToothRecordBo {

    private SnomedBo snomed;

    private String surfaceSctid;

    private LocalDate performedDate;

    public HCEToothRecordBo(String recordSctid, String recordPt, String surfaceSctid, LocalDate performedDate) {
        this.snomed = new SnomedBo();
        this.snomed.setSctid(recordSctid);
        this.snomed.setPt(recordPt);
        this.surfaceSctid = surfaceSctid;
        this.performedDate = performedDate;
    }

}
