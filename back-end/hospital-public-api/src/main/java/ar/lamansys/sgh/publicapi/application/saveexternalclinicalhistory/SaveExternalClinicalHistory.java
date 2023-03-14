package ar.lamansys.sgh.publicapi.application.saveexternalclinicalhistory;

import ar.lamansys.sgh.publicapi.application.port.out.ExternalClinicalHistoryStorage;
import ar.lamansys.sgh.publicapi.domain.ExternalClinicalHistoryBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveExternalClinicalHistory {

	private final ExternalClinicalHistoryStorage externalClinicalHistoryStorage;

	public Integer run(ExternalClinicalHistoryBo externalClinicalHistoryBo) {
		log.debug("Input parameter -> externalClinicalHistoryBo {}", externalClinicalHistoryBo);
		return externalClinicalHistoryStorage.create(externalClinicalHistoryBo);
	}

}
