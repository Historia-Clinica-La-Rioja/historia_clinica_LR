package net.pladema.internation.repository.ips.generalstate;

import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.masterdata.entity.Snomed;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InmunizationVo extends ClinicalTermVo{

    private LocalDate administrationDate;

    private Long noteId;

    private String note;

    public InmunizationVo(Integer id, Snomed snomed, String statusId, LocalDate administrationDate, Long noteId,
                              String note) {
        super(id, snomed, statusId);
        this.administrationDate = administrationDate;
        this.noteId = noteId;
        this.note = note;
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
		InmunizationVo other = (InmunizationVo) obj;
		return Objects.equals(noteId, other.getNoteId()) &&
			   Objects.equals(note, other.getNote());
	}
}
