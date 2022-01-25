package net.pladema.patient.repository;

import net.pladema.patient.repository.entity.PrivateHealthInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateHealthInsuranceRepository extends JpaRepository<PrivateHealthInsurance, Integer> {
}
