package net.pladema.emergencycare.servicerequest.service;

import net.pladema.clinichistory.requests.service.domain.ExtendedServiceRequestBo;

public interface CreateEmergencyCareServiceRequestService {

	Integer execute(ExtendedServiceRequestBo serviceRequest, Integer episodeId);

}
