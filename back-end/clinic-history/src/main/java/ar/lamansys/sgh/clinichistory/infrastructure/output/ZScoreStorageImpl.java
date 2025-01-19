package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.ZScoreStorage;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.ZScoreBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.ETimePeriod;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anthropometricgraphic.ZScoreRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anthropometricgraphic.entity.ZScore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ZScoreStorageImpl implements ZScoreStorage {

	private final ZScoreRepository zScoreRepository;

	@Override
	public List<ZScoreBo> getZScoreList(EAnthropometricGraphic graphic, EGender gender){
		log.debug("Input parameters -> graphic {}, gender {}", graphic, gender);
		List<ZScoreBo> result = zScoreRepository.getByGraphicIdAndGenderIdAndTimePeriodId(graphic.getId(), gender.getId(), ETimePeriod.MONTHLY.getId())
				.stream()
				.map(this::mapToBo)
				.collect(Collectors.toList());
		log.debug("Output -> result {}", result);
		return result;
	}

	private ZScoreBo mapToBo (ZScore entity){
		ZScoreBo result = new ZScoreBo();
		result.setXValue(entity.getXValue());
		result.setL(entity.getL());
		result.setM(entity.getM());
		result.setS(entity.getS());
		result.setSd(entity.getSd());
		result.setSd3negative(entity.getSd3Negative());
		result.setSd2negative(entity.getSd2Negative());
		result.setSd1negative(entity.getSd1Negative());
		result.setSd0(entity.getSd0());
		result.setSd1(entity.getSd1());
		result.setSd2(entity.getSd2());
		result.setSd3(entity.getSd3());
		result.setAnthropometricGraphic(EAnthropometricGraphic.map(entity.getAnthropometricGraphicId()));
		result.setGender(EGender.map(entity.getGenderId()));
		return result;
	}

}
