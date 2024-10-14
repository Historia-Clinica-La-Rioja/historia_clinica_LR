package ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class DiagnosesDto {
	private SnomedCIE10Dto main;
	private List<SnomedCIE10Dto> others;

	public DiagnosesDto() {
		this.others = new ArrayList<>();
	}

}
