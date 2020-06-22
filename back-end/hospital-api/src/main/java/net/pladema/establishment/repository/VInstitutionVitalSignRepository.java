package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.VInstitution;
import net.pladema.establishment.repository.entity.VInstitutionVitalSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VInstitutionVitalSignRepository extends JpaRepository<VInstitutionVitalSign, Long> {

    @Query("select i from VInstitutionVitalSign i where i.institutionId = :institutionId")
    List<VInstitutionVitalSign> getGeneralState(@Param("institutionId") Integer institutionId);
}
