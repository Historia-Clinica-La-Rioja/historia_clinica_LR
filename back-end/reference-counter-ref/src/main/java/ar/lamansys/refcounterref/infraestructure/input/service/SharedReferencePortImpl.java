package ar.lamansys.refcounterref.infraestructure.input.service;

import ar.lamansys.refcounterref.application.createcounterreference.CreateCounterReference;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceBo;
import ar.lamansys.refcounterref.infraestructure.input.rest.mapper.CounterReferenceMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferencePort;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceClosureDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class SharedReferencePortImpl implements SharedReferencePort {

	private final CreateCounterReference createCounterReference;
	private final CounterReferenceMapper counterReferenceMapper;

	@Override
	public void closeReference(ReferenceClosureDto counterReference, Integer institutionId, Integer patientId){
		if (counterReference == null)
			return;
		log.debug("Input parameter -> counterReference {}, institutionId {}, patientId {}", counterReference, institutionId, patientId);
		CounterReferenceBo counterReferenceBo = getCounterReferenceBo(counterReference, institutionId, patientId);
		createCounterReference.run(counterReferenceBo);
	}

	@Override
	public void validateReference(ReferenceClosureDto counterReference, Integer institutionId, Integer patientId){
		if (counterReference == null)
			return;
		log.debug("Input parameter -> counterReference {}, institutionId {}, patientId {}", counterReference, institutionId, patientId);
		CounterReferenceBo counterReferenceBo = getCounterReferenceBo(counterReference, institutionId, patientId);
		createCounterReference.runValidations(counterReferenceBo);
	}

	private CounterReferenceBo getCounterReferenceBo(ReferenceClosureDto counterReference, Integer institutionId, Integer patientId) {
		CounterReferenceBo counterReferenceBo = counterReferenceMapper.fromReferenceRequestClosureDto(counterReference);
		counterReferenceBo.setInstitutionId(institutionId);
		counterReferenceBo.setPatientId(patientId);
		if(counterReferenceBo.getCounterReferenceNote() == null)
			counterReferenceBo.setCounterReferenceNote("");
		return counterReferenceBo;
	}
}
