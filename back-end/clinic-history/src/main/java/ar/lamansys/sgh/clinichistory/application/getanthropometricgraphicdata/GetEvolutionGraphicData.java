package ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicdata;

import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricValueBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.GraphicDatasetInfoBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.GraphicDatasetIntersectionBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicLabel;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicRange;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEClinicalObservationBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GetEvolutionGraphicData {

	private final HCEClinicalObservationService hceClinicalObservationService;
	private final SharedPatientPort sharedPatientPort;

	public GraphicDatasetInfoBo run (Integer patientId, AnthropometricGraphicBo graphicBo, AnthropometricValueBo actualValue){
		List<GraphicDatasetIntersectionBo> result = new ArrayList<>();
		LocalDate patientBirthDate = sharedPatientPort.getBasicDataFromPatient(patientId).getBirthDate();
		if (graphicBo.getGraphic().equals(EAnthropometricGraphic.WEIGHT_FOR_HEIGHT) || graphicBo.getGraphic().equals(EAnthropometricGraphic.WEIGHT_FOR_LENGTH))
			result = getEvolutionIntersectionsByHeight(patientId, patientBirthDate, graphicBo, actualValue);
		else
			result = getEvolutionIntersectionsByAge(patientId, patientBirthDate, graphicBo, actualValue);

		return new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.EVOLUTION, result);
	}

	private List<GraphicDatasetIntersectionBo> getEvolutionIntersectionsByAge(Integer patientId, LocalDate patientBirthDate, AnthropometricGraphicBo graphicBo, AnthropometricValueBo anthropometricValue){
		List<HCEAnthropometricDataBo> patientEvolution = hceClinicalObservationService.getHistoricAnthropometricData(patientId);
		List<HCEClinicalObservationBo> evolutionValues = new ArrayList<>();
		if (graphicBo.getGraphic().equals(EAnthropometricGraphic.LENGTH_HEIGHT_FOR_AGE)) {
			evolutionValues = patientEvolution.stream().map(HCEAnthropometricDataBo::getHeight).filter(Objects::nonNull).collect(Collectors.toList());
		}
		if (graphicBo.getGraphic().equals(EAnthropometricGraphic.WEIGHT_FOR_AGE)) {
			evolutionValues = patientEvolution.stream().map(HCEAnthropometricDataBo::getWeight).filter(Objects::nonNull).collect(Collectors.toList());
		}
		if (graphicBo.getGraphic().equals(EAnthropometricGraphic.BMI_FOR_AGE)) {
			evolutionValues = patientEvolution.stream().map(HCEAnthropometricDataBo::getBmi).filter(Objects::nonNull).collect(Collectors.toList());
		}
		if (graphicBo.getGraphic().equals(EAnthropometricGraphic.HEAD_CIRCUMFERENCE)) {
			evolutionValues = patientEvolution.stream().map(HCEAnthropometricDataBo::getHeadCircumference).filter(Objects::nonNull).collect(Collectors.toList());
		}
		evolutionValues.sort(Comparator.comparing(HCEClinicalObservationBo::getEffectiveTime));
		List<GraphicDatasetIntersectionBo> evolutionIntersectionList = new ArrayList<>();
		Integer minWeeksOrMonths = (graphicBo.getRange().getValues().get(0));
		Integer maxWeeksOrMonths = (graphicBo.getRange().getValues().get(graphicBo.getRange().getValues().size() - 1));
		evolutionValues.forEach(evolution -> {
			GraphicDatasetIntersectionBo intersectionBo = getEvolutionIntersection(graphicBo.getRange(), evolution.getEffectiveTime().toLocalDate(), patientBirthDate, evolution.getValue().replace(',', '.'), minWeeksOrMonths, maxWeeksOrMonths);
			if (intersectionBo != null) evolutionIntersectionList.add(intersectionBo);
		});
		String actualValue = getActualValue(anthropometricValue, graphicBo.getGraphic());
		if (actualValue != null ){
			GraphicDatasetIntersectionBo intersectionBo = getEvolutionIntersection(graphicBo.getRange(), LocalDate.now(), patientBirthDate, actualValue.replace(',', '.'), minWeeksOrMonths, maxWeeksOrMonths);
			if (intersectionBo != null) evolutionIntersectionList.add(intersectionBo);
		}
		return evolutionIntersectionList;
	}

	private List<GraphicDatasetIntersectionBo> getEvolutionIntersectionsByHeight(Integer patientId, LocalDate patientBirthDate, AnthropometricGraphicBo graphicBo, AnthropometricValueBo actualValue){
		List<HCEAnthropometricDataBo> patientEvolution = hceClinicalObservationService.getHistoricAnthropometricData(patientId).stream()
				.filter(evolution -> evolution.getHeight() != null && evolution.getWeight() != null).collect(Collectors.toList());
		List<HCEClinicalObservationBo> heightEvolution = patientEvolution.stream().map(HCEAnthropometricDataBo::getHeight).sorted(Comparator.comparing(HCEClinicalObservationBo::getEffectiveTime)).collect(Collectors.toList());
		List<HCEClinicalObservationBo> weightEvolution = patientEvolution.stream().map(HCEAnthropometricDataBo::getWeight).sorted(Comparator.comparing(HCEClinicalObservationBo::getEffectiveTime)).collect(Collectors.toList());
		List<GraphicDatasetIntersectionBo> result = new ArrayList<>();
		Integer minHeightValue = graphicBo.getRange().getValues().get(0);
		Integer maxHeightValue = graphicBo.getRange().getValues().get(graphicBo.getRange().getValues().size() - 1);
		for (int i = 0; i< heightEvolution.size(); i++){
			int age = (int) ChronoUnit.YEARS.between(patientBirthDate, heightEvolution.get(i).getEffectiveTime());
			int heightValue = Integer.parseInt(heightEvolution.get(i).getValue());
			if (age >= graphicBo.getGraphic().getMinAge() && age < graphicBo.getGraphic().getMaxAge() && heightValue >= minHeightValue && heightValue <= maxHeightValue)
				result.add(new GraphicDatasetIntersectionBo(heightEvolution.get(i).getValue(), weightEvolution.get(i).getValue()));
		}
		if (actualValue != null && actualValue.getWeight() != null && actualValue.getHeight() != null){
			int age = (int) ChronoUnit.YEARS.between(patientBirthDate, LocalDate.now());
			int heightValue = Integer.parseInt(actualValue.getHeight());
			if (age >= graphicBo.getGraphic().getMinAge() && age < graphicBo.getGraphic().getMaxAge() && heightValue >= minHeightValue && heightValue <= maxHeightValue)
				result.add(new GraphicDatasetIntersectionBo(actualValue.getHeight(), actualValue.getWeight()));
		}
		return result;
	}

	private GraphicDatasetIntersectionBo getEvolutionIntersection(EAnthropometricGraphicRange range, LocalDate evolutionDate, LocalDate patientBirthDate, String value, Integer minXValue, Integer maxXValue){
		int x;
		if (range.equals(EAnthropometricGraphicRange.SIX_MONTHS)){
			x = (int) ChronoUnit.WEEKS.between(patientBirthDate, evolutionDate);
		}
		else {
			x = (int) ChronoUnit.MONTHS.between(patientBirthDate, evolutionDate);
		}
		if (x >= minXValue && x <= maxXValue){
			x = getClosestXValue(range.getValues(), x);
			return new GraphicDatasetIntersectionBo(String.valueOf(x), value);
		}
		return null;
	}

	private int getClosestXValue(List<Integer> rangeValues, int x){
		if (rangeValues.contains(x))
			return x;
		if (rangeValues.contains(x - 1))
			return x - 1;
		return x + 1;
	}

	private String getActualValue(AnthropometricValueBo anthropometricValue, EAnthropometricGraphic graphic){
		if (anthropometricValue == null)
			return null;
		if (graphic.equals(EAnthropometricGraphic.LENGTH_HEIGHT_FOR_AGE))
			return anthropometricValue.getHeight();
		if (graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_AGE))
			return anthropometricValue.getWeight();
		if (graphic.equals(EAnthropometricGraphic.BMI_FOR_AGE))
			return anthropometricValue.getBmi();
		if (graphic.equals(EAnthropometricGraphic.HEAD_CIRCUMFERENCE))
			return anthropometricValue.getHeadCircumference();
		return null;
	}

}
