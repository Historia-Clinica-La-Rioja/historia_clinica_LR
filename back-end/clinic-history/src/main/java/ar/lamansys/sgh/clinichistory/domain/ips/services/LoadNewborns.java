package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.domain.ips.NewbornBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.NewbornRepository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Newborn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LoadNewborns {

	private static final Logger LOG = LoggerFactory.getLogger(LoadNewborns.class);

	public static final String OUTPUT = "Output -> {}";

	private final NewbornRepository newbornRepository;

	public LoadNewborns(NewbornRepository newbornRepository){
		this.newbornRepository = newbornRepository;
	}

	public List<NewbornBo> run (List<NewbornBo> newborns, Integer obstetricEventId){
		LOG.debug("Input parameters -> newborns {}, obstetricEventId {}", newborns, obstetricEventId);
		if (newborns == null || obstetricEventId == null) return Collections.emptyList();
		newborns.forEach(nb -> {
			Newborn entity = new Newborn();
			entity.setWeight(nb.getWeight());
			entity.setGenderId(nb.getGenderId() != null ? nb.getGenderId().getId() : null);
			entity.setBirthConditionType(nb.getBirthConditionType() != null ? nb.getBirthConditionType().getId() : null);
			entity.setObstetricEventId(obstetricEventId);

			nb.setId(newbornRepository.save(entity).getId());

		});
		List<NewbornBo> result = newborns;
		LOG.debug(OUTPUT, result);
		return result;
	}

}

