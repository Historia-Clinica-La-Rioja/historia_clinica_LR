package net.pladema.clinichistory.ips.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.InmunizationVo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImmunizationBo extends ClinicalTerm {

    private LocalDate administrationDate;

    private String note;

    public ImmunizationBo(InmunizationVo inmunizationVo) {
        super();
        setId(inmunizationVo.getId());
        setStatusId(inmunizationVo.getStatusId());
        setStatus(inmunizationVo.getStatus());
        setSnomed(new SnomedBo(inmunizationVo.getSnomed()));
        setAdministrationDate(inmunizationVo.getAdministrationDate());
        setNote(inmunizationVo.getNote());
    }
}
