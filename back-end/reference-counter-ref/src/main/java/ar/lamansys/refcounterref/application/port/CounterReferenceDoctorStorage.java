package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.doctor.CounterReferenceDoctorInfoBo;

import java.util.Optional;

public interface CounterReferenceDoctorStorage {

    Optional<CounterReferenceDoctorInfoBo> getDoctorInfo();

}