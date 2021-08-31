package ar.lamansys.sgh.clinichistory.domain.hce;

import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMedicationVo;

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
