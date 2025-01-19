package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.reference.ReferencePatientBo;

public interface ReferencePatientStorage {

	ReferencePatientBo getPatientInfo(Integer patientId);

}
