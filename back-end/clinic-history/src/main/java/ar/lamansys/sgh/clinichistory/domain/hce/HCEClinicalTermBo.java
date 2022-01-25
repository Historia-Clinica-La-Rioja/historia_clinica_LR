package ar.lamansys.sgh.clinichistory.domain.hce;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class HCEClinicalTermBo implements Serializable {

    private Integer id;

    private SnomedBo snomed;

    private String statusId ;

    private String status;

    private Integer patientId;

    public HCEClinicalTermBo(Integer id, Snomed snomed, String statusId, String status, Integer patientId) {
        this.id = id;
        this.snomed = new SnomedBo(snomed);
        this.statusId = statusId;
        this.status = status;
        this.patientId = patientId;
    }

    public boolean isActive(){
        return statusId.equals(ConditionClinicalStatus.ACTIVE);
    }

    public String getSnomedSctid() {
        return this.snomed.getSctid();
    }

    public String getSnomedPt() {
        return this.snomed.getPt();
    }

}
