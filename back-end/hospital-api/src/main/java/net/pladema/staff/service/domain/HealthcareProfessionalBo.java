package net.pladema.staff.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HealthcareProfessionalBo extends BasicPersonalDataBo {

    private Integer id;

    private String licenceNumber;

    public HealthcareProfessionalBo(HealthcareProfessionalVo vo){
        super(vo.getFirstName(), vo.getLastName(), vo.getIdentificationNumber());
        this.id = vo.getId();
        this.licenceNumber = vo.getLicenceNumber();

    }
}
