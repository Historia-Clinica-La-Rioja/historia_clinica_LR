package net.pladema.internation.service.ips.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.ips.generalstate.InmunizationVo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InmunizationBo extends ClinicalTerm {

    private LocalDate administrationDate;

    private String note;

    public InmunizationBo(InmunizationVo inmunizationVo) {
        super();
        setId(inmunizationVo.getId());
        setStatusId(inmunizationVo.getStatusId());
        setStatus(inmunizationVo.getStatus());
        setSnomed(new SnomedBo(inmunizationVo.getSnomed()));
        setAdministrationDate(inmunizationVo.getAdministrationDate());
        setNote(inmunizationVo.getNote());
    }
}
