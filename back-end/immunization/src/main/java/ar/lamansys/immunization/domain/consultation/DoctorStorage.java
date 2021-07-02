package ar.lamansys.immunization.domain.consultation;

import ar.lamansys.immunization.domain.doctor.DoctorInfoBo;

import java.util.Optional;

public interface DoctorStorage {
    Optional<DoctorInfoBo> getDoctorInfo();
}
