package ar.lamansys.refcounterref.application.getreferencebyservicerequest;

import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.reference.ReferenceRequestBo;
import ar.lamansys.refcounterref.infraestructure.input.service.mapper.ReferenceMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetReferenceByServiceRequest {

	private final ReferenceStorage referenceStorage;

	public Optional<ReferenceRequestBo> run (Integer serviceRequestId){
		log.debug("Input parameters -> serviceRequestId {}", serviceRequestId);
		Optional<ReferenceRequestBo> result = referenceStorage.getReferenceByServiceRequestId(serviceRequestId);
		return result;
	}

}
