package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto;

import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicLabel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraphicDatasetInfoDto {

	private EAnthropometricGraphicLabel label;
	private List<GraphicDatasetIntersectionDto> intersections;

}
