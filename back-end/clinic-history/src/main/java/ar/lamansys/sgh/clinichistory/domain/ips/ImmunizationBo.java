package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ImmunizationVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImmunizationBo extends ClinicalTerm {

    private LocalDate administrationDate;

    private LocalDate expirationDate;

    private String note;

    private Integer institutionId;

    private String institutionInfo;

    private String doctorInfo;

    private ImmunizationDoseBo dose;

    private Short conditionId;

    private Short schemeId;

    private String lotNumber;

    private boolean billable = false;

    private Integer createdBy;

    public ImmunizationBo(Integer id, Integer patientId, SnomedBo snomed, String statusId,
                          LocalDate administrationDate, LocalDate expirationDate, Integer institutionId,
                          Short conditionId, Short schemeId, ImmunizationDoseBo dose,
                          String lotNumber, boolean billable, String note, Integer createdBy) {
        super(id, patientId, snomed, statusId, null, null);
        this.administrationDate = administrationDate;
        this.expirationDate = expirationDate;
        this.note = note;
        this.institutionId = institutionId;
        this.dose = dose;
        this.conditionId = conditionId;
        this.schemeId = schemeId;
        this.lotNumber = lotNumber;
        this.billable = billable;
        this.createdBy = createdBy;
    }

    public ImmunizationBo(@Valid @NotNull(message = "{value.mandatory}") SnomedBo snomed, LocalDate administrationDate, String note, Integer institutionId, ImmunizationDoseBo dose, Short conditionId, Short schemeId, String lotNumber, boolean billable) {
        super(snomed);
        this.administrationDate = administrationDate;
        this.note = note;
        this.institutionId = institutionId;
        this.dose = dose;
        this.conditionId = conditionId;
        this.schemeId = schemeId;
        this.lotNumber = lotNumber;
        this.billable = billable;
    }

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

	@Override
	public boolean equals (ClinicalTerm c){
		boolean datesAreEquals = Optional.ofNullable(((ImmunizationBo)c).getAdministrationDate())
				.map(c1 -> Optional.ofNullable(getAdministrationDate())
						.map(c2-> c2.equals(c1))
						.orElse(false))
				.orElseGet(()-> getAdministrationDate()==null);
		return super.equals(c)&&datesAreEquals;
	}
}
