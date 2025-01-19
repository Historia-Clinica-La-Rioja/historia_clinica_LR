package net.pladema.patient.infrastructure.output.repository;

import net.pladema.patient.infrastructure.output.repository.entity.GeographicallyLocatedPatient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeographicallyLocatedPatientRepository extends JpaRepository<GeographicallyLocatedPatient, Integer> {
}
