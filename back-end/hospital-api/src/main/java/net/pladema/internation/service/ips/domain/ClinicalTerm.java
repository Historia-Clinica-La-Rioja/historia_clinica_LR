package net.pladema.internation.service.ips.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.masterdata.entity.ConditionClinicalStatus;

import java.io.Serializable;

@Getter
@Setter
@ToString
public abstract class ClinicalTerm implements Serializable {

    private Integer id;

    private String statusId ;

    private SnomedBo snomed;

    private String status;


    public boolean isActive(){
        return statusId.equals(ConditionClinicalStatus.ACTIVE);
    }

}
