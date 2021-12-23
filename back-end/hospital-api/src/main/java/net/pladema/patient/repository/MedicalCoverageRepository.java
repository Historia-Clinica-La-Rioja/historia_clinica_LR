package net.pladema.patient.repository;

import net.pladema.patient.repository.entity.MedicalCoverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MedicalCoverageRepository extends JpaRepository<MedicalCoverage, Integer> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT mc FROM MedicalCoverage mc WHERE upper(mc.name) = upper(:name)")
    List<MedicalCoverage> getByName(@Param("name") String name);
}
