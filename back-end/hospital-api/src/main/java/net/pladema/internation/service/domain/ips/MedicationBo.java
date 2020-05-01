package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.ips.generalstate.MedicationVo;
import net.pladema.internation.service.domain.SnomedBo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicationBo extends ClinicalTerm {

    private String note;

    public MedicationBo(MedicationVo medicationVo) {
        super();
        setId(medicationVo.getId());
        setStatusId(medicationVo.getStatusId());
        setSnomed(new SnomedBo(medicationVo.getSnomed()));
        setNote(medicationVo.getNote());
    }
}
