package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class DiagnosesDto {
	private SnomedDto main;
	private List<SnomedDto> others;

	public DiagnosesDto() {
		this.others = new ArrayList<>();
	}

	public void addOther(SnomedDto snomed) {
		if(others == null) {
			others = new ArrayList<>();
		}
		others.add(snomed);
	}

	public void remove(SnomedDto snomed) {
		others.remove(snomed);
	}
}
