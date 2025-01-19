package ar.lamansys.sgh.publicapi.patient.application.port.out;

import ar.lamansys.sgh.publicapi.patient.domain.ExternalClinicalHistoryBo;

public interface ExternalClinicalHistoryStorage {

	Integer create(ExternalClinicalHistoryBo externalClinicalHistoryBo);
}
