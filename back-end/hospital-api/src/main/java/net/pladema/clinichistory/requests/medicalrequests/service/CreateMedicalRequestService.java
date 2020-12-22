package net.pladema.clinichistory.requests.medicalrequests.service;

import net.pladema.clinichistory.requests.medicalrequests.service.domain.MedicalRequestBo;

public interface CreateMedicalRequestService {

    Integer execute(Integer institutionId, MedicalRequestBo medicalRequest);

}
