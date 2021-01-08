package net.pladema.clinichistory.documents.service.hce.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEMedicationVo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.service.ips.domain.DosageBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEMedicationBo extends HCEClinicalTermBo {

    private boolean suspended = false;

    private DosageBo dosage;

    public HCEMedicationBo(HCEMedicationVo hceMedicationVo) {
        super();
        setId(hceMedicationVo.getId());
        setStatusId(hceMedicationVo.getStatusId());
        setStatus(hceMedicationVo.getStatus());
        setSnomed(new SnomedBo(hceMedicationVo.getSnomed()));
        suspended = super.getStatusId().equalsIgnoreCase(MedicationStatementStatus.SUSPENDED);
        dosage = new DosageBo();
        dosage.setId(hceMedicationVo.getDosageId());
        dosage.setStartDate(hceMedicationVo.getStartDate());
        dosage.setEndDate(hceMedicationVo.getEndDate());
        dosage.setSuspendedStartDate(hceMedicationVo.getSuspendedStartDate());
        dosage.setSuspendedEndDate(hceMedicationVo.getSuspendedEndDate());
    }

    @Override
    public String getStatusId(){
        return suspended ? MedicationStatementStatus.SUSPENDED : super.getStatusId();
    }


}
