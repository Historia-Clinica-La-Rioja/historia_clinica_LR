package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.ips.generalstate.InmunizationVo;
import net.pladema.internation.service.domain.SnomedBo;

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
        setSnomed(new SnomedBo(inmunizationVo.getSnomed()));
        setAdministrationDate(inmunizationVo.getAdministrationDate());
        setNote(inmunizationVo.getNote());
    }
}
