package net.pladema.patient.repository;


import net.pladema.patient.repository.entity.PrivateHealthInsuranceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateHealthInsuranceDetailsRepository extends JpaRepository<PrivateHealthInsuranceDetails, Integer> {}
