package net.pladema.patient.service;

import net.pladema.patient.service.domain.DoctorsBo;

public interface AdditionalDoctorService {

    DoctorsBo addAdditionalsDoctors(DoctorsBo doctorsBo,Integer patientId);
    
    DoctorsBo getAdditionalsDoctors(Integer patientId);
    
}
