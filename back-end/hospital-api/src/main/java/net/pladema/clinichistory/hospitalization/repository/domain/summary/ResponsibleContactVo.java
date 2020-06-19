package net.pladema.clinichistory.hospitalization.repository.domain.summary;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.repository.domain.ResponsibleContact;

@Getter
@Setter
public class ResponsibleContactVo {

    private String fullName;

    private String phoneNumber;

    private String relationship;

    public ResponsibleContactVo (ResponsibleContact rc){
        this.fullName = rc.getFullName();
        this.phoneNumber = rc.getPhoneNumber();
        this.relationship = rc.getRelationship();
    }
}
