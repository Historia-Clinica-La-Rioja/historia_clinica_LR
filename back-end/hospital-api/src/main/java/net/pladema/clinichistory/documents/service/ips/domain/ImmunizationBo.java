package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.generalstate.domain.ImmunizationVo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImmunizationBo extends ClinicalTerm {

    private LocalDate administrationDate;

    private String note;

    private Integer institutionId;

    public ImmunizationBo(ImmunizationVo immunizationVo) {
        super();
        setId(immunizationVo.getId());
        setStatusId(immunizationVo.getStatusId());
        setStatus(immunizationVo.getStatus());
        setSnomed(new SnomedBo(immunizationVo.getSnomed()));
        setAdministrationDate(immunizationVo.getAdministrationDate());
        setNote(immunizationVo.getNote());
    }

    public ImmunizationBo(SnomedBo snomedBo) {
        super(snomedBo);
    }
}
