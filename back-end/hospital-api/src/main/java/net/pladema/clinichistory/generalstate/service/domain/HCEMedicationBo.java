package net.pladema.clinichistory.generalstate.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.generalstate.repository.domain.HCEMedicationVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.ips.service.domain.SnomedBo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEMedicationBo extends HCEClinicalTermBo {

    private boolean suspended = false;

    public HCEMedicationBo(HCEMedicationVo hceMedicationVo) {
        super();
        setId(hceMedicationVo.getId());
        setStatusId(hceMedicationVo.getStatusId());
        setStatus(hceMedicationVo.getStatus());
        setSnomed(new SnomedBo(hceMedicationVo.getSnomed()));
        suspended = super.getStatusId().equalsIgnoreCase(MedicationStatementStatus.SUSPENDED);
    }

    @Override
    public String getStatusId(){
        return suspended ? MedicationStatementStatus.SUSPENDED : super.getStatusId();
    }
}
