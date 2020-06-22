package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.VInstitution;
import net.pladema.establishment.repository.entity.VInstitutionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VInstitutionRepository extends JpaRepository<VInstitution, VInstitutionPK> {

    @Query("select i from VInstitution i where i.pk.institutionId = :institutionId")
    List<VInstitution> getGeneralState(@Param("institutionId") Integer institutionId);

}
