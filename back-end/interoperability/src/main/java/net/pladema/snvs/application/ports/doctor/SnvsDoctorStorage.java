package net.pladema.snvs.application.ports.doctor;

import net.pladema.snvs.domain.doctor.DoctorInfoBo;

import java.util.Optional;

public interface SnvsDoctorStorage {
    Optional<DoctorInfoBo> getDoctorInfo();
}
