package net.pladema.clinichistory.documents.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CHDocumentNotesSummary {

	private String currentIllness;
	private String physicalExam;
	private String evolution;
	private String clinicalImpression;
	private String otherNote;
	private String indicationNote;
	private String observations;

	public List<String> getList(){
		List<String> result = Arrays.asList(currentIllness, physicalExam, evolution, clinicalImpression, otherNote);
		return result.stream()
				.filter(note -> note != null && !note.isBlank()).collect(Collectors.toList());
	}

}
