package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineConditionApplicationRepository extends JpaRepository<VaccineConditionApplication, Short> {

}