package net.pladema.patient.repository;

import net.pladema.patient.repository.entity.AdditionalDoctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalDoctorRepository extends JpaRepository<AdditionalDoctor, Integer> {
}
