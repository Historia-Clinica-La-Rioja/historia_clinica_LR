package net.pladema.clinichistory.hospitalization.service.domain;


import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.ResponsibleContactVo;

@Getter
@Setter
public class ResponsibleContactBo {

    private String fullName;

    private String phoneNumber;

    private String relationship;

    public ResponsibleContactBo(ResponsibleContactVo responsibleContactVo){
        this.fullName = responsibleContactVo.getFullName();
        this.phoneNumber = responsibleContactVo.getPhoneNumber();
        this.relationship = responsibleContactVo.getRelationship();
    }
}
