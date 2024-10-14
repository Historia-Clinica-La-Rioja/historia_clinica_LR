package ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicdata;

import ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicdata.exceptions.GetAnthropometricGraphicDataException;
import ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicdata.exceptions.GetAnthropometricGraphicDataExceptionEnum;
import ar.lamansys.sgh.clinichistory.application.ports.ZScoreStorage;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicDataBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricValueBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicRange;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.GraphicDatasetInfoBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.GraphicDatasetIntersectionBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.ZScoreBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicType;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.PatientGenderAgeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetAnthropometricGraphicData {

	private static final Short MAX_PATIENT_AGE = 18;
	private static final Integer WEEKS_IN_MONTH = 4;
	private static final Integer MONTHS_IN_YEAR = 12;

	private final SharedPatientPort sharedPatientPort;
	private final GetPercentilesGraphicData getPercentilesGraphicData;
	private final GetZScoreGraphicData getZScoreGraphicData;
	private final GetEvolutionGraphicData getEvolutionGraphicData;
	private final ZScoreStorage zScoreStorage;

	public AnthropometricGraphicDataBo run (Integer patientId, EAnthropometricGraphic graphic, EAnthropometricGraphicType graphicType, AnthropometricValueBo anthropometricActualValue){
		PatientGenderAgeDto patientGenderAgeDto = sharedPatientPort.getPatientGenderAge(patientId)
				.orElseThrow(() -> new GetAnthropometricGraphicDataException(GetAnthropometricGraphicDataExceptionEnum.INVALID_PATIENT_ID, String.format("El paciente con id %s no existe", patientId)));
		assertValidPatientAge(patientGenderAgeDto);
		EGender gender = null;
		EAnthropometricGraphicRange graphicRange = getGraphicRange(patientGenderAgeDto, graphic);
		if (patientGenderAgeDto.getGender() != null)
			gender = EGender.map(patientGenderAgeDto.getGender().getId());

		AnthropometricGraphicBo graphicBo = new AnthropometricGraphicBo(graphic, graphicType, graphicRange, gender);

		AnthropometricGraphicDataBo result = loadGraphicData(graphicBo, patientGenderAgeDto, anthropometricActualValue);

		log.debug("Output -> result {}", result);
		return result;
	}

	private boolean assertValidPatientAge(PatientGenderAgeDto patientGenderAgeDto){
		if (patientGenderAgeDto.getAge() == null || patientGenderAgeDto.getAge().getYears() > MAX_PATIENT_AGE)
			throw new GetAnthropometricGraphicDataException(GetAnthropometricGraphicDataExceptionEnum.INVALID_PATIENT_AGE, "El paciente debe tener entre 0 y 19 años");
		return true;
	}

	private AnthropometricGraphicDataBo loadGraphicData(AnthropometricGraphicBo graphicBo, PatientGenderAgeDto patient, AnthropometricValueBo anthropometricValueBo){
		AnthropometricGraphicDataBo result = new AnthropometricGraphicDataBo();
		result.setDatasetInfo(new ArrayList<>());
		result.setGraphicRange(graphicBo.getRange());
		result.setXAxisRange(graphicBo.getRange().getValues().stream().map(String::valueOf).collect(Collectors.toList()));
		setGraphicAxisLabels(result, graphicBo);
		result.setXAxisRangeLabels(getXAxisRangeLabels(graphicBo.getRange()));
		setEvolutionGraphicData(patient, result, graphicBo, anthropometricValueBo);
		if (graphicBo.getGraphicType().equals(EAnthropometricGraphicType.PERCENTILES))
			result.getDatasetInfo().addAll(getPercentilesGraphicData.run(graphicBo));
		if (graphicBo.getGraphicType().equals(EAnthropometricGraphicType.ZSCORE))
			result.getDatasetInfo().addAll(getZScoreGraphicData.run(graphicBo));
		return result;

	}

	private void setEvolutionGraphicData(PatientGenderAgeDto patient, AnthropometricGraphicDataBo graphicDataBo, AnthropometricGraphicBo graphicBo, AnthropometricValueBo actualValue){
		GraphicDatasetInfoBo evolutionInfo = getEvolutionGraphicData.run(patient.getId(), graphicBo, actualValue);
		if (graphicBo.getGraphicType().equals(EAnthropometricGraphicType.ZSCORE))
            graphicDataBo.setEvolutionZScoreValues(getEvolutionZScoreValues(evolutionInfo.getIntersections(), graphicBo));
		graphicDataBo.getDatasetInfo().add(evolutionInfo);
	}

	private EAnthropometricGraphicRange getGraphicRange(PatientGenderAgeDto patientGenderAgeDto, EAnthropometricGraphic graphic){
		Short patientYears = patientGenderAgeDto.getAge().getYears();
		Short patientMonths = patientGenderAgeDto.getAge().getMonths();
		boolean hasGender = patientGenderAgeDto.getGender() != null && !patientGenderAgeDto.getGender().getId().equals(EGender.X.getId());
		if (graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_LENGTH))
			return EAnthropometricGraphicRange.WEIGHT_FOR_LENGTH;
		if (graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_HEIGHT))
			return EAnthropometricGraphicRange.WEIGHT_FOR_HEIGHT;
		if (patientYears > 4 && !graphic.equals(EAnthropometricGraphic.HEAD_CIRCUMFERENCE)){
			if (graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_AGE)) return EAnthropometricGraphicRange.TEN_YEARS;
			if (graphic.equals(EAnthropometricGraphic.BMI_FOR_AGE) && hasGender) return EAnthropometricGraphicRange.TWO_TO_NINETEEN_YEARS;
			return EAnthropometricGraphicRange.NINETEEN_YEARS;
		}
		if (patientYears < 1 && patientMonths < 6 && !graphic.equals(EAnthropometricGraphic.BMI_FOR_AGE))
			return EAnthropometricGraphicRange.SIX_MONTHS;
		if (patientYears < 2 && graphic.equals(EAnthropometricGraphic.BMI_FOR_AGE))
			return EAnthropometricGraphicRange.TWO_YEARS;
		if (patientYears > 1 && graphic.equals(EAnthropometricGraphic.BMI_FOR_AGE) && hasGender)
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

}
