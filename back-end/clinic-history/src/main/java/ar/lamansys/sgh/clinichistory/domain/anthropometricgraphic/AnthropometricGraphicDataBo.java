package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic;


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
public class AnthropometricGraphicDataBo {

	private List<String> xAxisRange;
	private List<String> xAxisRangeLabels;
	private List<GraphicDatasetInfoBo> datasetInfo;
	private EAnthropometricGraphicRange graphicRange;
	private List<String> evolutionZScoreValues;
	private String xAxisLabel;
	private String yAxisLabel;

}
