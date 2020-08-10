package net.pladema.patient.service;

import net.pladema.patient.service.domain.DoctorsBo;

public interface AdditionalDoctorService {

    DoctorsBo addAdditionalDoctors(DoctorsBo doctorsBo, Integer patientId);
    
    DoctorsBo getAdditionalDoctors(Integer patientId);
    
}
