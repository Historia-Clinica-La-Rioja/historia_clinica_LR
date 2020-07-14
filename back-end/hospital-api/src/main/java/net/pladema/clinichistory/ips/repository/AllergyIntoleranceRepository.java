package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.entity.AllergyIntolerance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyIntoleranceRepository extends JpaRepository<AllergyIntolerance, Integer> {

}
