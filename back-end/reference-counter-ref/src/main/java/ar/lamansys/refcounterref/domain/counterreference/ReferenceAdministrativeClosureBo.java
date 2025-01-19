package ar.lamansys.refcounterref.domain.counterreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ReferenceAdministrativeClosureBo {

	private Integer patientId;

	private Integer institutionId;

	@NotNull
	private Integer referenceId;

	@NotNull
	private String closureNote;

	private LocalDate date;

	private List<Integer> fileIds;


	public ReferenceAdministrativeClosureBo(Integer institutionId, Integer referenceId, String closureNote, List<Integer> fileIds) {
		this.institutionId = institutionId;
		this.referenceId = referenceId;
		this.closureNote = closureNote;
		this.fileIds = fileIds;
		this.date = LocalDate.now();
	}
}
