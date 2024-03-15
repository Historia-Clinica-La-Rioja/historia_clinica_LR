package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public enum EAnthropometricGraphicRange {

	SIX_MONTHS(IntStream.rangeClosed(0, 180).boxed().collect(Collectors.toList())),
	FIVE_YEARS(IntStream.rangeClosed(0, 60).boxed().collect(Collectors.toList())),
	NINETEEN_YEARS(IntStream.rangeClosed(0, 228).filter(i -> i % 3 == 0).boxed().collect(Collectors.toList()));

	private List<Integer> values;

	EAnthropometricGraphicRange(List<Integer> values){
		this.values = values;
	}

}
