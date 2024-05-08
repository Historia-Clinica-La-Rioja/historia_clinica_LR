package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.DocumentPercentilesStorage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentPercentilesRepository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentPercentiles;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class DocumentPercentilesStorageImpl implements DocumentPercentilesStorage {

	private final DocumentPercentilesRepository documentPercentilesRepository;

	public Integer save(Long documentId, Integer percentilesId, Double percentilesValue){
		log.debug("Input parameters -> documentId {}, percentilesId {}, percentilesValue {}", documentId, percentilesId, percentilesValue);
		Integer result = documentPercentilesRepository.save(new DocumentPercentiles(null, documentId, percentilesId, percentilesValue)).getId();
		return result;
	}

}
