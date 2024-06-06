package ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicdata;

import ar.lamansys.sgh.clinichistory.application.fetchHCE.HCEClinicalObservationService;
import ar.lamansys.sgh.clinichistory.application.ports.PercentilesStorage;
import ar.lamansys.sgh.clinichistory.application.ports.ZScoreStorage;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicDataBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricValueBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicLabel;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicRange;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.GraphicDatasetInfoBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.GraphicDatasetIntersectionBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.PercentilesBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.ZScoreBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicType;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PersonAgeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetAnthropometricGraphicData {

	private static final Integer WEEKS_IN_MONTH = 4;
	private static final Integer MONTHS_IN_YEAR = 12;

	private final PercentilesStorage percentilesStorage;
	private final ZScoreStorage zScoreStorage;
	private final SharedPatientPort sharedPatientPort;
	private final HCEClinicalObservationService hceClinicalObservationService;

	public AnthropometricGraphicDataBo run (Integer patientId, EAnthropometricGraphic graphic, EAnthropometricGraphicType graphicType, AnthropometricValueBo anthropometricActualValue){
		BasicPatientDto basicDataPatient = sharedPatientPort.getBasicDataFromPatient(patientId);
		EGender gender = null;
		EAnthropometricGraphicRange graphicRange = getGraphicRange(basicDataPatient, graphic);
		if (graphicRange == null)
			return new AnthropometricGraphicDataBo();

		if (basicDataPatient.getPerson().getGenderId() != null)
			gender = EGender.map(basicDataPatient.getPerson().getGenderId());

		AnthropometricGraphicBo graphicBo = new AnthropometricGraphicBo(graphic, graphicType, graphicRange, gender);

		AnthropometricGraphicDataBo result = initializeGraphicData(graphicBo, basicDataPatient, anthropometricActualValue);

		log.debug("Output -> result {}", result);
		return result;
	}

	private AnthropometricGraphicDataBo initializeGraphicData(AnthropometricGraphicBo graphicBo, BasicPatientDto patient, AnthropometricValueBo anthropometricValueBo){
		AnthropometricGraphicDataBo result = new AnthropometricGraphicDataBo();
		result.setDatasetInfo(new ArrayList<>());
		result.setGraphicRange(graphicBo.getRange());
		result.setXAxisRange(graphicBo.getRange().getValues().stream().map(String::valueOf).collect(Collectors.toList()));
		setGraphicAxisLabels(result, graphicBo);
		result.setXAxisRangeLabels(getXAxisRangeLabels(graphicBo.getRange()));
		setEvolutionGraphicData(patient, result, graphicBo, anthropometricValueBo);
		if (graphicBo.getGraphicType().equals(EAnthropometricGraphicType.PERCENTILES))
			setPercentilesGraphicData(result, graphicBo);
		if (graphicBo.getGraphicType().equals(EAnthropometricGraphicType.ZSCORE))
			setZScoreGraphicData(result, graphicBo);
		return result;

	}

	private void setEvolutionGraphicData(BasicPatientDto patient, AnthropometricGraphicDataBo graphicDataBo, AnthropometricGraphicBo graphicBo, AnthropometricValueBo actualValue){
		List<GraphicDatasetIntersectionBo> evolutionIntersectionList;
		if (graphicBo.getGraphic().equals(EAnthropometricGraphic.WEIGHT_FOR_HEIGHT) || graphicBo.getGraphic().equals(EAnthropometricGraphic.WEIGHT_FOR_LENGTH))
			evolutionIntersectionList = getEvolutionIntersectionsByHeight(patient, graphicBo, actualValue);
		else
			evolutionIntersectionList = getEvolutionIntersectionsByAge(patient, graphicBo, actualValue);

		if (graphicBo.getGraphicType().equals(EAnthropometricGraphicType.ZSCORE))
            graphicDataBo.setEvolutionZScoreValues(getEvolutionZScoreValues(evolutionIntersectionList, graphicBo));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.EVOLUTION, evolutionIntersectionList));
	}

	private List<GraphicDatasetIntersectionBo> getEvolutionIntersectionsByAge(BasicPatientDto patient, AnthropometricGraphicBo graphicBo, AnthropometricValueBo anthropometricValue){
		List<HCEAnthropometricDataBo> patientEvolution = hceClinicalObservationService.getHistoricAnthropometricData(patient.getId());
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
			GraphicDatasetIntersectionBo intersectionBo = getEvolutionIntersection(graphicBo.getRange(), evolution.getEffectiveTime().toLocalDate(), patient.getBirthDate(), evolution.getValue().replace(',', '.'), minWeeksOrMonths, maxWeeksOrMonths);
			if (intersectionBo != null) evolutionIntersectionList.add(intersectionBo);
		});
		String actualValue = getActualValue(anthropometricValue, graphicBo.getGraphic());
		if (actualValue != null ){
			GraphicDatasetIntersectionBo intersectionBo = getEvolutionIntersection(graphicBo.getRange(), LocalDate.now(), patient.getBirthDate(), actualValue.replace(',', '.'), minWeeksOrMonths, maxWeeksOrMonths);
			if (intersectionBo != null) evolutionIntersectionList.add(intersectionBo);
		}
		return evolutionIntersectionList;
	}

	private List<GraphicDatasetIntersectionBo> getEvolutionIntersectionsByHeight(BasicPatientDto patient, AnthropometricGraphicBo graphicBo, AnthropometricValueBo actualValue){
		List<HCEAnthropometricDataBo> patientEvolution = hceClinicalObservationService.getHistoricAnthropometricData(patient.getId()).stream()
				.filter(evolution -> evolution.getHeight() != null && evolution.getWeight() != null).collect(Collectors.toList());
		List<HCEClinicalObservationBo> heightEvolution = patientEvolution.stream().map(HCEAnthropometricDataBo::getHeight).sorted(Comparator.comparing(HCEClinicalObservationBo::getEffectiveTime)).collect(Collectors.toList());
		List<HCEClinicalObservationBo> weightEvolution = patientEvolution.stream().map(HCEAnthropometricDataBo::getWeight).sorted(Comparator.comparing(HCEClinicalObservationBo::getEffectiveTime)).collect(Collectors.toList());
		List<GraphicDatasetIntersectionBo> result = new ArrayList<>();
		Integer minHeightValue = graphicBo.getRange().getValues().get(0);
		Integer maxHeightValue = graphicBo.getRange().getValues().get(graphicBo.getRange().getValues().size() - 1);
		for (int i = 0; i< heightEvolution.size(); i++){
			int age = (int) ChronoUnit.YEARS.between(patient.getBirthDate(), heightEvolution.get(i).getEffectiveTime());
			int heightValue = Integer.parseInt(heightEvolution.get(i).getValue());
			if (age >= graphicBo.getGraphic().getMinAge() && age < graphicBo.getGraphic().getMaxAge() && heightValue >= minHeightValue && heightValue <= maxHeightValue)
				result.add(new GraphicDatasetIntersectionBo(heightEvolution.get(i).getValue(), weightEvolution.get(i).getValue()));
		}
		if (actualValue != null && actualValue.getWeight() != null && actualValue.getHeight() != null){
			int age = (int) ChronoUnit.YEARS.between(patient.getBirthDate(), LocalDate.now());
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

	private void setPercentilesGraphicData(AnthropometricGraphicDataBo graphicDataBo, AnthropometricGraphicBo graphicBo){
		List<PercentilesBo> percentilesList = percentilesStorage.getPercentilesList(graphicBo.getGraphic(), graphicBo.getGender());
		List<GraphicDatasetIntersectionBo> p3IntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> p10IntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> p25IntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> p50IntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> p75IntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> p90IntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> p97IntersectionsList = new ArrayList<>();
		boolean inWeeks = graphicBo.getRange().equals(EAnthropometricGraphicRange.SIX_MONTHS);
		List<Integer> rangeValues = graphicBo.getRange().getValues();
		percentilesList.forEach(percentile -> {
			if (percentile.getXValue() % 1 != 0)
				return;
			Integer x = (int) (inWeeks ? percentile.getXValue() * WEEKS_IN_MONTH : percentile.getXValue());
			if (!rangeValues.contains(x))
				return;
			String xValue = String.valueOf(x);
			p3IntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, percentile.getP3().toString()));
			p10IntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, percentile.getP10().toString()));
			p25IntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, percentile.getP25().toString()));
			p50IntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, percentile.getP50().toString()));
			p75IntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, percentile.getP75().toString()));
			p90IntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, percentile.getP90().toString()));
			p97IntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, percentile.getP97().toString()));
		});
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.P97, p97IntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.P90, p90IntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.P75, p75IntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.P50, p50IntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.P25, p25IntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.P10, p10IntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.P3, p3IntersectionsList));
	}

	private void setZScoreGraphicData(AnthropometricGraphicDataBo graphicDataBo, AnthropometricGraphicBo graphicBo){
		List<ZScoreBo> zScoreList = zScoreStorage.getZScoreList(graphicBo.getGraphic(), graphicBo.getGender());
		List<GraphicDatasetIntersectionBo> sd3NegativeIntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> sd2NegativeIntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> sd1NegativeIntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> sd0IntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> sd1IntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> sd2IntersectionsList = new ArrayList<>();
		List<GraphicDatasetIntersectionBo> sd3IntersectionsList = new ArrayList<>();
		boolean inWeeks = graphicBo.getRange().equals(EAnthropometricGraphicRange.SIX_MONTHS);
		List<Integer> rangeValues = graphicBo.getRange().getValues();
		zScoreList.forEach(z -> {
			if (z.getXValue() % 1 != 0)
				return;
			Integer x = (int) (inWeeks ? z.getXValue() * WEEKS_IN_MONTH : z.getXValue());
			if (!rangeValues.contains(x))
				return;
			String xValue = String.valueOf(x);
			sd3NegativeIntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, z.getSd3negative().toString()));
			sd2NegativeIntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, z.getSd2negative().toString()));
			sd1NegativeIntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, z.getSd1negative().toString()));
			sd0IntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, z.getSd0().toString()));
			sd1IntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, z.getSd1().toString()));
			sd2IntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, z.getSd2().toString()));
			sd3IntersectionsList.add(new GraphicDatasetIntersectionBo(xValue, z.getSd3().toString()));
		});
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.SD3, sd3IntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.SD2, sd2IntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.SD1, sd1IntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.SD0, sd0IntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.SD1NEGATIVE, sd1NegativeIntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.SD2NEGATIVE, sd2NegativeIntersectionsList));
		graphicDataBo.getDatasetInfo().add(new GraphicDatasetInfoBo(EAnthropometricGraphicLabel.SD3NEGATIVE, sd3NegativeIntersectionsList));
	}

	private int getClosestXValue(List<Integer> rangeValues, int x){
		if (rangeValues.contains(x))
			return x;
		if (rangeValues.contains(x - 1))
			return x - 1;
		return x + 1;
	}

	private EAnthropometricGraphicRange getGraphicRange(BasicPatientDto basicPatientDto, EAnthropometricGraphic graphic){
		PersonAgeDto personAge = basicPatientDto.getPerson().getPersonAge();
		boolean hasGender = basicPatientDto.getGender() != null && !basicPatientDto.getGender().getId().equals(EGender.X.getId());
		if (personAge == null || personAge.getYears() > 18)
			return null;
		if (graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_LENGTH))
			return EAnthropometricGraphicRange.WEIGHT_FOR_LENGTH;
		if (graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_HEIGHT))
			return EAnthropometricGraphicRange.WEIGHT_FOR_HEIGHT;
		if (personAge.getYears() > 4 && !graphic.equals(EAnthropometricGraphic.HEAD_CIRCUMFERENCE)){
			if (graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_AGE)) return EAnthropometricGraphicRange.TEN_YEARS;
			if (graphic.equals(EAnthropometricGraphic.BMI_FOR_AGE) && hasGender) return EAnthropometricGraphicRange.TWO_TO_NINETEEN_YEARS;
			return EAnthropometricGraphicRange.NINETEEN_YEARS;
		}
		if (personAge.getYears() < 1 && personAge.getMonths() < 6 && !graphic.equals(EAnthropometricGraphic.BMI_FOR_AGE))
			return EAnthropometricGraphicRange.SIX_MONTHS;
		if (personAge.getYears() < 2 && graphic.equals(EAnthropometricGraphic.BMI_FOR_AGE))
			return EAnthropometricGraphicRange.TWO_YEARS;
		if (personAge.getYears() > 1 && graphic.equals(EAnthropometricGraphic.BMI_FOR_AGE) && hasGender)
			return EAnthropometricGraphicRange.TWO_TO_FIVE_YEARS;
		return EAnthropometricGraphicRange.FIVE_YEARS;
	}

	private void setGraphicAxisLabels(AnthropometricGraphicDataBo anthropometricGraphicDataBo, AnthropometricGraphicBo graphicBo){
		EAnthropometricGraphic graphic = graphicBo.getGraphic();
		if (graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_HEIGHT) || graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_LENGTH))
			anthropometricGraphicDataBo.setXAxisLabel("Talla (cm)");
		else
			anthropometricGraphicDataBo.setXAxisLabel(graphicBo.getRange().equals(EAnthropometricGraphicRange.SIX_MONTHS) ? "Edad (en semanas y meses cumplidos)" : "Edad (en meses y años cumplidos)");
		if (graphic.equals(EAnthropometricGraphic.LENGTH_HEIGHT_FOR_AGE))
			anthropometricGraphicDataBo.setYAxisLabel("Talla (cm)");
		if (graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_AGE) || graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_LENGTH) || graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_HEIGHT))
			anthropometricGraphicDataBo.setYAxisLabel("Peso (kg)");
		if (graphic.equals(EAnthropometricGraphic.BMI_FOR_AGE))
			anthropometricGraphicDataBo.setYAxisLabel("IMC (kg/m²)");
		if (graphic.equals(EAnthropometricGraphic.HEAD_CIRCUMFERENCE))
			anthropometricGraphicDataBo.setYAxisLabel("Perímetro cefálico (cm)");
	}


	private List<String> getXAxisRangeLabels(EAnthropometricGraphicRange graphicRange){
		List<Integer> xAxisRange = graphicRange.getValues();
		List<String> xAxisRangeLabels = new ArrayList<>();
		if (graphicRange.equals(EAnthropometricGraphicRange.SIX_MONTHS)){
			xAxisRange.forEach(x -> xAxisRangeLabels.add(getSixMonthsLabel(x)));
		}
		if (graphicRange.equals(EAnthropometricGraphicRange.TWO_YEARS)){
			xAxisRange.forEach(x -> xAxisRangeLabels.add(getTwoYearsLabel(x)));
		}
		if (graphicRange.equals(EAnthropometricGraphicRange.FIVE_YEARS) || graphicRange.equals(EAnthropometricGraphicRange.TWO_TO_FIVE_YEARS)){
			xAxisRange.forEach(x -> xAxisRangeLabels.add(getFiveYearsLabel(x)));
		}
		if (graphicRange.equals(EAnthropometricGraphicRange.NINETEEN_YEARS) || graphicRange.equals(EAnthropometricGraphicRange.TEN_YEARS) || graphicRange.equals(EAnthropometricGraphicRange.TWO_TO_NINETEEN_YEARS)) {
			xAxisRange.forEach(x -> xAxisRangeLabels.add(getNineteenYearsLabel(x)));
		}
		if (graphicRange.equals(EAnthropometricGraphicRange.WEIGHT_FOR_LENGTH) || graphicRange.equals(EAnthropometricGraphicRange.WEIGHT_FOR_HEIGHT)){
			xAxisRange.forEach(x -> xAxisRangeLabels.add(x % 5 == 0 ? x.toString() : ""));
		}
		return xAxisRangeLabels;
	}

	private String getSixMonthsLabel(Integer x){
		if (x == 0)
			return "Nacimiento";
		else {
			if (x % WEEKS_IN_MONTH == 0)
				 return String.valueOf(x/WEEKS_IN_MONTH).concat(x/WEEKS_IN_MONTH == 1 ? " mes" : " meses");
			else
				return x.toString().concat(x == 1 ? " semana" : " semanas");
		}
	}

	private String getTwoYearsLabel(Integer x){
		if (x == 0)
			return "Nacimiento";
		else {
			if (x % MONTHS_IN_YEAR == 0)
				return String.valueOf(x/MONTHS_IN_YEAR).concat(x/MONTHS_IN_YEAR == 1 ? " año" : " años");
			else
				return x.toString().concat(x == 1 ? " mes" : " meses");
		}
	}

	private String getFiveYearsLabel(Integer x){
		if (x == 0)
			return "Nacimiento";
		else {
			if (x % MONTHS_IN_YEAR == 0)
				return String.valueOf(x/MONTHS_IN_YEAR).concat(x.equals(MONTHS_IN_YEAR) ? " año" : " años");
			else {
				if (x % 2 == 0) {
					return String.valueOf(x % MONTHS_IN_YEAR);
				} else {
					return Strings.EMPTY;
				}
			}
		}
	}

	private String getNineteenYearsLabel(Integer x) {
		if (x == 0)
			return "Nacimiento";
		else {
			if (x % 12 == 0)
				return String.valueOf(x/MONTHS_IN_YEAR).concat(x.equals(MONTHS_IN_YEAR) ? " año" : " años");
			else
				return "";
		}
	}

	private List<String> getEvolutionZScoreValues(List<GraphicDatasetIntersectionBo> intersections, AnthropometricGraphicBo graphicBo){
		List<ZScoreBo> zScoreList = zScoreStorage.getZScoreList(graphicBo.getGraphic(), graphicBo.getGender());
		List<String> result = new ArrayList<>();
		intersections.forEach(evolution -> {
			int x = Integer.parseInt(evolution.getX()) * (graphicBo.getRange().equals(EAnthropometricGraphicRange.SIX_MONTHS) ? WEEKS_IN_MONTH : 1);
			var zScoreValue = zScoreList.stream().filter(zScoreBo -> zScoreBo.getXValue() == x).findFirst();
			zScoreValue.ifPresent(zScoreBo -> {
				double evolutionZScore = ((Math.pow((Double.parseDouble(evolution.getY()) / zScoreBo.getM()), zScoreBo.getL())) - 1) / (zScoreBo.getL() * zScoreBo.getS());
				DecimalFormat df = new DecimalFormat("#.####");
				result.add(df.format(evolutionZScore));
			});
		});
		return result;
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
