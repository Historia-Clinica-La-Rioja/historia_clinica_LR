package net.pladema.internation.service.internment.summary.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.internation.repository.internment.domain.ResponsibleContact;

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
