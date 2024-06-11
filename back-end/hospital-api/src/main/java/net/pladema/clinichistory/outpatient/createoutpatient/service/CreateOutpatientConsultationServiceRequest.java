package net.pladema.clinichistory.outpatient.createoutpatient.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;

public interface CreateOutpatientConsultationServiceRequest {

	Integer execute(Integer doctorId, String categoryId, BasicPatientDto patientDto, Integer institutionId,
		Integer healthConditionId, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid,
		String snomedPt, Boolean createAsFinal);
}
