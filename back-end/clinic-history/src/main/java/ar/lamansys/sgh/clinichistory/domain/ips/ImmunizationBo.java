package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ImmunizationVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImmunizationBo extends ClinicalTerm {

    private LocalDate administrationDate;

    private String note;

    private Integer institutionId;

    private ImmunizationDoseBo dose;

    private Short conditionId;

    private Short schemeId;

    private String lotNumber;

    private boolean billable = false;

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
