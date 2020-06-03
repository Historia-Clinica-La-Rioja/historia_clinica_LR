package net.pladema.internation.service.internment.summary.domain;


import lombok.Getter;
import lombok.Setter;

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
