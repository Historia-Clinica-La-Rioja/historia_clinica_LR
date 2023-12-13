package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import ar.lamansys.sgh.clinichistory.application.healthCondition.HealthConditionStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHealthConditionPort;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class SharedHealthConditionPortImpl implements SharedHealthConditionPort {

	private final HealthConditionStorage healthConditionStorage;

	@Override
	public Integer getHealthConditionIdByEncounterAndSnomedConcept(Integer encounterId, Integer sourceTypeId, String sctid, String pt) {
		log.debug("Input parameters -> encounterId {}, sourceTypeId {}, sctid {}, pt {} ", encounterId, sctid, sctid, pt);
		return healthConditionStorage.getHealthConditionIdByEncounterAndSnomedConcept(encounterId, sourceTypeId, sctid, pt).get(0);
	}
}
