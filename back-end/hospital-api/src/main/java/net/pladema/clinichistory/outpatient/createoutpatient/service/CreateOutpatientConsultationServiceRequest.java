package net.pladema.clinichistory.outpatient.createoutpatient.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions.CreateOutpatientConsultationServiceRequestException;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.AddObservationsCommandVo;

import java.util.Optional;

public interface CreateOutpatientConsultationServiceRequest {

	Integer execute(Integer doctorId, String categoryId, BasicPatientDto patientDto, Integer institutionId,
					Integer healthConditionId, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid,
					String snomedPt, Boolean createAsFinal, Optional<AddObservationsCommandVo> addObservationsCommand) throws CreateOutpatientConsultationServiceRequestException;
}
