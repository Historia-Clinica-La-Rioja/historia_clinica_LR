package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

@Getter
public enum EAnthropometricGraphicRange {

	SIX_MONTHS(IntStream.rangeClosed(0, 24).boxed().collect(Collectors.toList())),
	FIVE_YEARS(IntStream.rangeClosed(0, 60).boxed().collect(Collectors.toList())),
	TEN_YEARS(IntStream.rangeClosed(0, 120).filter(i -> i % 3 == 0).boxed().collect(Collectors.toList())),
	NINETEEN_YEARS(IntStream.rangeClosed(0, 228).filter(i -> i % 3 == 0).boxed().collect(Collectors.toList())),
	WEIGHT_FOR_LENGTH(IntStream.rangeClosed(45, 110).boxed().collect(Collectors.toList())),
	WEIGHT_FOR_HEIGHT(IntStream.rangeClosed(65, 120).boxed().collect(Collectors.toList())),
	TWO_TO_FIVE_YEARS(IntStream.rangeClosed(24, 60).boxed().collect(Collectors.toList())),
	TWO_TO_NINETEEN_YEARS(IntStream.rangeClosed(24, 228).filter(i -> i % 3 == 0).boxed().collect(Collectors.toList())),
	TWO_YEARS(IntStream.rangeClosed(0,24).boxed().collect(Collectors.toList()));

	private List<Integer> values;

	EAnthropometricGraphicRange(List<Integer> values){
		this.values = values;
	}

}
