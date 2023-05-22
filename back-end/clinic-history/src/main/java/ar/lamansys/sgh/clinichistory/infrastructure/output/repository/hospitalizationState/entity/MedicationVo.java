package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicationVo extends ClinicalTermVo{

    private Long noteId;

    private String note;

	private Integer prescriptionLineNumber;

	private Dosage dosage;

	private Double quantityValue;

	private String quantityUnit;

	private Snomed healthConditionDiagnosis;

    public MedicationVo(Integer id, Snomed snomed, String statusId, Long noteId,
						String note) {
        super(id, snomed, statusId);
        this.noteId = noteId;
        this.note = note;
    }

	public MedicationVo(Integer id, Snomed snomed, String statusId, String status, Long noteId,
						String note) {
		this(id, snomed, statusId, noteId, note);
		this.setStatus(status);
	}

	public MedicationVo(Integer id, Snomed snomed, String statusId, Long noteId,
						String note, Integer prescriptionLineNumber, Dosage dosage, Double quantityValue, String quantityUnit, Snomed healthConditionDiagnosis) {
		super(id, snomed, statusId);
		this.noteId = noteId;
		this.note = note;
		this.prescriptionLineNumber = prescriptionLineNumber;
		this.dosage = dosage;
		this.quantityValue = quantityValue;
		this.quantityUnit = quantityUnit;
		this.healthConditionDiagnosis = healthConditionDiagnosis;
	}
    
	@Override
	public int hashCode() {
		return Objects.hash(noteId, note);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedicationVo other = (MedicationVo) obj;
		return super.equals(other) &&
			   Objects.equals(noteId, other.getNoteId()) &&
			   Objects.equals(note, other.getNote());
	}
}
