package ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicdata;

import ar.lamansys.sgh.clinichistory.application.ports.ZScoreStorage;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.GraphicDatasetInfoBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.GraphicDatasetIntersectionBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.ZScoreBo;
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
public class GetZScoreGraphicData {

	private static final Integer WEEKS_IN_MONTH = 4;

	private final ZScoreStorage zScoreStorage;

	public List<GraphicDatasetInfoBo> run(AnthropometricGraphicBo graphicBo) {
		log.debug("Input parameters -> graphicBo {}", graphicBo);
		List<ZScoreBo> zScoreList = zScoreStorage.getZScoreList(graphicBo.getGraphic(), graphicBo.getGender());
		Map<EAnthropometricGraphicLabel, List<GraphicDatasetIntersectionBo>> intersectionsMap = createIntersectionsMap(zScoreList, graphicBo);
		List<GraphicDatasetInfoBo> result = createGraphicDatasetInfoBoList(intersectionsMap);
		log.debug("Output -> result {}", result);
		return result;
	}

	private Map<EAnthropometricGraphicLabel, List<GraphicDatasetIntersectionBo>> createIntersectionsMap(List<ZScoreBo> zScoreList, AnthropometricGraphicBo graphicBo) {
		Map<EAnthropometricGraphicLabel, List<GraphicDatasetIntersectionBo>> intersectionsMap = new EnumMap<>(EAnthropometricGraphicLabel.class);
		for (EAnthropometricGraphicLabel label : EAnthropometricGraphicLabel.getZScoreLabels()) {
			intersectionsMap.put(label, new ArrayList<>());
		}
		boolean inWeeks = graphicBo.getRange().equals(EAnthropometricGraphicRange.SIX_MONTHS);
		List<Integer> rangeValues = graphicBo.getRange().getValues();
		zScoreList.forEach(z -> {
			if (z.getXValue() % 1 != 0) {
				return;
			}
			Integer x = (int) (inWeeks ? z.getXValue() * WEEKS_IN_MONTH : z.getXValue());
			if (!rangeValues.contains(x)) {
				return;
			}
			String xValue = String.valueOf(x);
			intersectionsMap.get(EAnthropometricGraphicLabel.SD3).add(new GraphicDatasetIntersectionBo(xValue, z.getSd3().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.SD2).add(new GraphicDatasetIntersectionBo(xValue, z.getSd2().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.SD1).add(new GraphicDatasetIntersectionBo(xValue, z.getSd1().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.SD0).add(new GraphicDatasetIntersectionBo(xValue, z.getSd0().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.SD1NEGATIVE).add(new GraphicDatasetIntersectionBo(xValue, z.getSd1negative().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.SD2NEGATIVE).add(new GraphicDatasetIntersectionBo(xValue, z.getSd2negative().toString()));
			intersectionsMap.get(EAnthropometricGraphicLabel.SD3NEGATIVE).add(new GraphicDatasetIntersectionBo(xValue, z.getSd3negative().toString()));
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
