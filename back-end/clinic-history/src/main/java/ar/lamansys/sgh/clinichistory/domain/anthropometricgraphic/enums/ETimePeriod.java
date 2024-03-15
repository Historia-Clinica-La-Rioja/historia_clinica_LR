package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums;

import lombok.Getter;

@Getter
public enum ETimePeriod {

	MONTHLY ((short)1),
	WEEKLY ((short)2),
	NO_PERIOD ((short)3);

	private Short id;

	ETimePeriod(Number id){
		this.id = id.shortValue();
	}
	
}
