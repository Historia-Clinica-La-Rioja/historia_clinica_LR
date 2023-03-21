package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.domain.ExternalClinicalHistoryBo;

public interface ExternalClinicalHistoryStorage {

	Integer create(ExternalClinicalHistoryBo externalClinicalHistoryBo);
}
