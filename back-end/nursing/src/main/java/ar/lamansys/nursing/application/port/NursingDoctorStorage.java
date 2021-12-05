package ar.lamansys.nursing.application.port;

import ar.lamansys.nursing.domain.doctor.DoctorInfoBo;

import java.util.Optional;

public interface NursingDoctorStorage {

    Optional<DoctorInfoBo> getDoctorInfo();

}
