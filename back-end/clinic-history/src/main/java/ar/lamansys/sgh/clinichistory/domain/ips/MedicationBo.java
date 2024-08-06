package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.visitor.IpsVisitor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.MedicationVo;

import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicationBo extends ClinicalTerm implements IpsBo {

    private Long noteId;

    private String note;

    private boolean suspended = false;

    private DosageBo dosage;

    private HealthConditionBo healthCondition;

    private Integer encounterId;

    private boolean hasRecipe;

    private Integer userId;

    private LocalDate createdOn;

	private Boolean isDigital;

	private Integer prescriptionLineNumber;

	private LocalDate prescriptionDate;
	
	private LocalDate dueDate;

	private BigInteger relatedDocumentId;

	private String relatedDocumentName;

	private Short prescriptionLineState;

	private CommercialMedicationPrescriptionBo commercialMedicationPrescription;

    public MedicationBo(MedicationVo medicationVo) {
        super();
        setId(medicationVo.getId());
        setStatusId(medicationVo.getStatusId());
        setStatus(medicationVo.getStatus());
        setSnomed(new SnomedBo(medicationVo.getSnomed()));
        setNote(medicationVo.getNote());
		if (medicationVo.getPrescriptionLineNumber() != null)
			setPrescriptionLineNumber(medicationVo.getPrescriptionLineNumber());
		if (medicationVo.getDosage() != null)
			setDosage(new DosageBo(medicationVo.getDosage(), medicationVo.getQuantityValue(), medicationVo.getQuantityUnit()));
		if (medicationVo.getHealthConditionDiagnosis() != null)
			setHealthCondition(new HealthConditionBo(new SnomedBo(medicationVo.getHealthConditionDiagnosis())));
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

    @Override
    public void accept(IpsVisitor visitor) {
        visitor.visitMedication(this);
    }
}
