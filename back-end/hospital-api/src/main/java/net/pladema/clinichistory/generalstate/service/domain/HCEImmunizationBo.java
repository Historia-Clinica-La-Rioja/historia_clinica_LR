package net.pladema.clinichistory.generalstate.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.generalstate.repository.domain.HCEImmunizationVo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEImmunizationBo extends  HCEClinicalTermBo{

    private LocalDate administrationDate;

    public HCEImmunizationBo(HCEImmunizationVo source){
        super(source.getId(), source.getSnomed(), source.getStatusId(), source.getStatus(), source.getPatientId());
        this.administrationDate = source.getAdministrationDate();
    }
}
