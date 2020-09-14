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

    private String licenceNumber;

    public HealthcareProfessionalBo(HealthcareProfessional vo, Person p){
        super(p.getFirstName(), p.getLastName(), p.getIdentificationNumber());
        this.id = vo.getId();
        this.licenceNumber = vo.getLicenseNumber();
    }

    public HealthcareProfessionalBo(HealthcareProfessionalVo vo){
        super(vo.getFirstName(), vo.getLastName(), vo.getIdentificationNumber());
        this.id = vo.getId();
        this.licenceNumber = vo.getLicenceNumber();
    }
}
