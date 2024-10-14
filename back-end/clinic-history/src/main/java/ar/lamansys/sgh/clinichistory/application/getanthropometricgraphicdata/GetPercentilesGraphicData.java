package ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicdata;

import ar.lamansys.sgh.clinichistory.application.ports.PercentilesStorage;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.GraphicDatasetInfoBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.GraphicDatasetIntersectionBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.PercentilesBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicLabel;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicRange;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class GetPercentilesGraphicData {

	private static final Integer WEEKS_IN_MONTH = 4;

	private final PercentilesStorage percentilesStorage;

	public List<GraphicDatasetInfoBo> run (AnthropometricGraphicBo graphicBo) {
		log.debug("Input parameters -> graphicBo {}", graphicBo);
		List<PercentilesBo> percentilesList = percentilesStorage.getPercentilesList(graphicBo.getGraphic(), graphicBo.getGender());
		Map<EAnthropometricGraphicLabel, List<GraphicDatasetIntersectionBo>> intersectionsMap = createIntersectionsMap(percentilesList, graphicBo);
		List<GraphicDatasetInfoBo> result = createGraphicDatasetInfoBoList(intersectionsMap);
		log.debug("Output -> result {}", result);
		return result;
	}

	private Map<EAnthropometricGraphicLabel, List<GraphicDatasetIntersectionBo>> createIntersectionsMap(List<PercentilesBo> percentilesList, AnthropometricGraphicBo graphicBo){
		Map<EAnthropometricGraphicLabel, List<GraphicDatasetIntersectionBo>> intersectionsMap = new EnumMap<>(EAnthropometricGraphicLabel.class);
		for (EAnthropometricGraphicLabel label : EAnthropometricGraphicLabel.getPercentilesLabels()) {
			intersectionsMap.put(label, new ArrayList<>());
		}
		boolean inWeeks = graphicBo.getRange().equals(EAnthropometricGraphicRange.SIX_MONTHS);
		List<Integer> rangeValues = graphicBo.getRange().getValues();
		percentilesList.forEach(p -> {
			if (p.getXValue() % 1 != 0) {
				return;
			}
			Integer x = (int) (inWeeks ? p.getXValue() * WEEKS_IN_MONTH : p.getXValue());
			if (!rangeValues.contains(x)) {
				return;
			}
			String xValue = String.valueOf(x);
			intersectionsMap.get(EAnthropometricGraphicLabel.P97).add(new GraphicDatasetIntersectionBo(xValue, p.getP97().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.P90).add(new GraphicDatasetIntersectionBo(xValue, p.getP90().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.P75).add(new GraphicDatasetIntersectionBo(xValue, p.getP75().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.P50).add(new GraphicDatasetIntersectionBo(xValue, p.getP50().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.P25).add(new GraphicDatasetIntersectionBo(xValue, p.getP25().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.P10).add(new GraphicDatasetIntersectionBo(xValue, p.getP10().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.P3).add(new GraphicDatasetIntersectionBo(xValue, p.getP3().toString()));
		});
		return intersectionsMap;
	}

	private List<GraphicDatasetInfoBo> createGraphicDatasetInfoBoList(Map<EAnthropometricGraphicLabel, List<GraphicDatasetIntersectionBo>> intersectionsMap) {
		List<GraphicDatasetInfoBo> result = new ArrayList<>();
		List<EAnthropometricGraphicLabel> keys = intersectionsMap.keySet().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		for (EAnthropometricGraphicLabel key: keys) {
			result.add(new GraphicDatasetInfoBo(key, intersectionsMap.get(key)));
		}
		return result;
	}

}
