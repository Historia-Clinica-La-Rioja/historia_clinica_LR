package net.pladema.staff.service.domain;

import lombok.*;
import net.pladema.person.repository.entity.Person;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.repository.entity.HealthcareProfessional;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HealthcareProfessionalBo extends BasicPersonalDataBo {

    private Integer id;

    private Integer personId;

    private String licenceNumber;

    public HealthcareProfessionalBo(HealthcareProfessional vo, Person p){
        super(p.getFirstName(), p.getLastName(), p.getIdentificationNumber(), null);
        this.id = vo.getId();
        this.licenceNumber = vo.getLicenseNumber();
        this.personId = p.getId();
    }

    public HealthcareProfessionalBo(HealthcareProfessionalVo vo){
        super(vo.getFirstName(), vo.getLastName(), vo.getIdentificationNumber(), vo.getNameSelfDetermination());
        this.id = vo.getId();
        this.licenceNumber = vo.getLicenceNumber();
        this.personId = vo.getPersonId();
    }
}
