package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
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
