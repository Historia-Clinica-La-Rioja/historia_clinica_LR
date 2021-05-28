package ar.lamansys.sgh.clinichistory.domain.hce;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEImmunizationVo;

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
