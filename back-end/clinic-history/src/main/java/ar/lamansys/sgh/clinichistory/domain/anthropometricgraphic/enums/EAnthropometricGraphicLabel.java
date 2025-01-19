package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

import java.util.List;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EAnthropometricGraphicLabel {

	P3((short)1, "p3"), P10((short)2,"p10"), P25((short)3,"p25"), P50((short)4,"p50"), P75((short)5,"p75"), P90((short)6,"p90"), P97((short)7,"p97"),
	SD3NEGATIVE((short)8,"-3"), SD2NEGATIVE((short)9,"-2"), SD1NEGATIVE((short)10,"-1"), SD0((short)11,"0"), SD1((short)12,"1"), SD2((short)13,"2"), SD3((short)14,"3"),
	EVOLUTION((short)15,"Evolución");

	private Short id;
	private String value;

	EAnthropometricGraphicLabel(Number id, String value){
		this.id = id.shortValue();
		this.value = value;
	}

	public static List<EAnthropometricGraphicLabel> getZScoreLabels(){
		return List.of(SD3, SD2, SD1, SD0, SD1NEGATIVE, SD2NEGATIVE, SD3NEGATIVE);
	}

	public static List<EAnthropometricGraphicLabel> getPercentilesLabels(){
		return List.of(P97, P90, P75, P50, P25, P10, P3);
	}

}
