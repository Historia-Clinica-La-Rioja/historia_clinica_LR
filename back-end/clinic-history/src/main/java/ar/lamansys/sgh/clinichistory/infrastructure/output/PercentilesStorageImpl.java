package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.PercentilesStorage;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.PercentilesBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.ETimePeriod;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anthropometricgraphic.PercentilesRepository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anthropometricgraphic.entity.Percentiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PercentilesStorageImpl implements PercentilesStorage {

	private final PercentilesRepository percentilesRepository;

	@Override
	public List<PercentilesBo> getPercentilesList(EAnthropometricGraphic graphic, EGender gender) {
		log.debug("Input parameters -> graphic {}, gender {}", graphic, gender);
		List<Percentiles> percentiles;
		if (graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_LENGTH) || graphic.equals(EAnthropometricGraphic.WEIGHT_FOR_HEIGHT))
			percentiles = percentilesRepository.getByGraphicIdAndGenderIdAndTimePeriodId(graphic.getId(), gender.getId(), ETimePeriod.NO_PERIOD.getId());
		else
			percentiles = percentilesRepository.getByGraphicIdAndGenderIdAndTimePeriodId(graphic.getId(), gender.getId(), ETimePeriod.MONTHLY.getId());
		List<PercentilesBo> result = percentiles.stream().map(this::mapToBo).collect(Collectors.toList());
		log.debug("Output -> result {}", result);
		return result;
	}

	private PercentilesBo mapToBo(Percentiles entity){
		PercentilesBo result = new PercentilesBo();
		result.setId(entity.getId());
		result.setXValue(entity.getXValue());
		result.setP3(entity.getP3());
		result.setP10(entity.getP10());
		result.setP25(entity.getP25());
		result.setP50(entity.getP50());
		result.setP75(entity.getP75());
		result.setP90(entity.getP90());
		result.setP97(entity.getP97());
		result.setGender(EGender.map(entity.getGenderId()));
		result.setAnthropometricGraphic(EAnthropometricGraphic.map(entity.getAnthropometricGraphicId()));
		return result;
	}

}
