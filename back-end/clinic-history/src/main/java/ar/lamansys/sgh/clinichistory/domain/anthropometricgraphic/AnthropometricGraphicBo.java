package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic;

import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicRange;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicType;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnthropometricGraphicBo {

	private EAnthropometricGraphic graphic;
	private EAnthropometricGraphicType graphicType;
	private EAnthropometricGraphicRange range;
	private EGender gender;

}
