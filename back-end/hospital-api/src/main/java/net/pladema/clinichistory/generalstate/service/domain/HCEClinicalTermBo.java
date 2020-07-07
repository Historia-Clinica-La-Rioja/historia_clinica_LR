package net.pladema.clinichistory.generalstate.service.domain;

import lombok.*;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;
import net.pladema.clinichistory.ips.service.domain.SnomedBo;

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

}
