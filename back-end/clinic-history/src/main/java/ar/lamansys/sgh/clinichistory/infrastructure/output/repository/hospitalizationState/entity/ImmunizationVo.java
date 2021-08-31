package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImmunizationVo extends ClinicalTermVo{

    private LocalDate administrationDate;

    private Long noteId;

    private String note;

    public ImmunizationVo(Integer id, Snomed snomed, String statusId, LocalDate administrationDate, Long noteId,
						  String note) {
        super(id, snomed, statusId);
        this.administrationDate = administrationDate;
        this.noteId = noteId;
        this.note = note;
    }

	public ImmunizationVo(Integer id, Snomed snomed, String statusId, String status,
						  LocalDate administrationDate, Long noteId, String note) {
		this(id, snomed, statusId, administrationDate, noteId, note);
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
		ImmunizationVo other = (ImmunizationVo) obj;
		return Objects.equals(noteId, other.getNoteId()) &&
			   Objects.equals(note, other.getNote());
	}
}
