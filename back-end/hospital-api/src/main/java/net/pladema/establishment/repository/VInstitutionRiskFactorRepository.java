package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.VInstitutionRiskFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VInstitutionRiskFactorRepository extends JpaRepository<VInstitutionRiskFactor, Long> {

    @Query("select i from VInstitutionRiskFactor i where i.institutionId = :institutionId")
    List<VInstitutionRiskFactor> getGeneralState(@Param("institutionId") Integer institutionId);
}
