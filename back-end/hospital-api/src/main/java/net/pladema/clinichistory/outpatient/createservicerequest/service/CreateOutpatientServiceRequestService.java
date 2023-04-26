package net.pladema.clinichistory.outpatient.createservicerequest.service;

import net.pladema.clinichistory.requests.service.domain.ExtendedServiceRequestBo;

public interface CreateOutpatientServiceRequestService {

	Integer execute(ExtendedServiceRequestBo extendedServiceRequestBo);

}
