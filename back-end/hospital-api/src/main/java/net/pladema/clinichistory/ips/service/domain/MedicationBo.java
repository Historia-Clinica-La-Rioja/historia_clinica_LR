package net.pladema.clinichistory.ips.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.repository.generalstate.MedicationVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.MedicationStatementStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicationBo extends ClinicalTerm {

    private String note;

    private boolean suspended = false;

    public MedicationBo(MedicationVo medicationVo) {
        super();
        setId(medicationVo.getId());
        setStatusId(medicationVo.getStatusId());
        setStatus(medicationVo.getStatus());
        setSnomed(new SnomedBo(medicationVo.getSnomed()));
        setNote(medicationVo.getNote());
        suspended = super.getStatusId().equalsIgnoreCase(MedicationStatementStatus.SUSPENDED);
    }

    @Override
    public String getStatusId(){
        return suspended ? MedicationStatementStatus.SUSPENDED : super.getStatusId();
    }

}
