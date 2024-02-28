package net.pladema.establishment.application.rules.determineregulatedreference;

import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceException;
import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.port.RuleStorage;

import net.pladema.establishment.application.rules.determineregulatedreference.exceptions.RuleException;

import net.pladema.establishment.application.rules.determineregulatedreference.exceptions.RuleExceptionEnum;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DetermineRegulatedReference {

	private final RuleStorage ruleStorage;

	public boolean run(Integer institutionId, List<Integer> clinicalSpecialtyIds, Integer practiceId) {
		log.debug("Input parameters -> institutionId {}, clinicalSpecialtyIds {}, practiceId {}, ", institutionId, clinicalSpecialtyIds, practiceId);
		assertContextValid(institutionId, clinicalSpecialtyIds, practiceId);
		if (practiceId != null)
			return ruleStorage.isPracticeRegulated(practiceId, institutionId);
		else
			return ruleStorage.isAnyClinicalSpecialtyRegulated(clinicalSpecialtyIds, institutionId);
	}

	private void assertContextValid(Integer institutionId, List<Integer> clinicalSpecialtyIds, Integer practiceId) {
		if (institutionId == null)
			throw new RuleException(RuleExceptionEnum.NULL_INSTITUTION_ID, "El id de la institución es obligatorio");
		if ((clinicalSpecialtyIds == null || clinicalSpecialtyIds.isEmpty()) && practiceId == null)
			throw new RuleException(RuleExceptionEnum.NULL_MINIMAL_DATA, "Debe ingresar una practica o al menos una especialidad para corroborar la regulación de la misma");
	}

}
