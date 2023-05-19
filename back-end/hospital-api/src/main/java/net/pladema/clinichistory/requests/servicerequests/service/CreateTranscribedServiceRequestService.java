package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import net.pladema.clinichistory.requests.controller.dto.TranscribedPrescriptionDto;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;

public interface CreateTranscribedServiceRequestService {

    Integer execute(TranscribedPrescriptionDto transcribedPrescriptionDto, BasicPatientDto patientDto);
}
