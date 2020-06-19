package net.pladema.clinichistory.hospitalization.service.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.repository.domain.ResponsibleContact;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.ResponsibleContactVo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibleContactBo {

    private String fullName;

    private String phoneNumber;

    private String relationship;

    public ResponsibleContactBo(ResponsibleContactVo responsibleContactVo){
        this.fullName = responsibleContactVo.getFullName();
        this.phoneNumber = responsibleContactVo.getPhoneNumber();
        this.relationship = responsibleContactVo.getRelationship();
    }

    public ResponsibleContactBo(ResponsibleContact responsibleContact){
        this.fullName = responsibleContact.getFullName();
        this.phoneNumber = responsibleContact.getPhoneNumber();
        this.relationship = responsibleContact.getRelationship();
    }

}
