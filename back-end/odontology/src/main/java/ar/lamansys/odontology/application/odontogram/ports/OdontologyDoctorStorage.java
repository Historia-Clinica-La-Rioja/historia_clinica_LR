package ar.lamansys.odontology.application.odontogram.ports;

import ar.lamansys.odontology.domain.consultation.DoctorInfoBo;

import java.util.Optional;

public interface OdontologyDoctorStorage {

    Optional<DoctorInfoBo> getDoctorInfo();

}
