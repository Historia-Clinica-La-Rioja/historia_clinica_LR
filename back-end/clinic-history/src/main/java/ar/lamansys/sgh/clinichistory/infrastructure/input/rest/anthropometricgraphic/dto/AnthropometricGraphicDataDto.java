package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto;

import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicRange;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnthropometricGraphicDataDto {

	private List<String> xAxisRange;
	private List<String> xAxisRangeLabels;
	private List<GraphicDatasetInfoDto> datasetInfo;
	private EAnthropometricGraphicRange graphicRange;
	private List<String> evolutionZScoreValues;
	private String xAxisLabel;
	private String yAxisLabel;

}
