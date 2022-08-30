package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.MedicationVo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicationBo extends ClinicalTerm {

    private Long noteId;

    private String note;

    private boolean suspended = false;

    private DosageBo dosage;

    private HealthConditionBo healthCondition;

    private Integer encounterId;

    private boolean hasRecipe;

    private Integer userId;

    private LocalDate createdOn;

    public MedicationBo(MedicationVo medicationVo) {
        super();
        setId(medicationVo.getId());
        setStatusId(medicationVo.getStatusId());
        setStatus(medicationVo.getStatus());
        setSnomed(new SnomedBo(medicationVo.getSnomed()));
        setNote(medicationVo.getNote());
        suspended = super.getStatusId().equalsIgnoreCase(MedicationStatementStatus.SUSPENDED);
    }

    public MedicationBo(SnomedBo snomedBo) {
        super(snomedBo);
    }

    @Override
    public String getStatusId(){
        String statusId = super.getStatusId() != null ? super.getStatusId() : MedicationStatementStatus.ACTIVE;
        return suspended ? MedicationStatementStatus.SUSPENDED : statusId;
    }

	@Override
	public boolean equals(ClinicalTerm c){
		return super.equals(c)&&
				((MedicationBo)c).isSuspended()==isSuspended();
	}

}
