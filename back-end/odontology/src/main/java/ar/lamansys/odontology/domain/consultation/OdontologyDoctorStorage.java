package ar.lamansys.odontology.domain.consultation;

import java.util.Optional;

public interface OdontologyDoctorStorage {

    Optional<DoctorInfoBo> getDoctorInfo();

}
