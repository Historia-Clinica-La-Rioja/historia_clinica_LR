package net.pladema.patient.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.patient.repository.entity.MedicalCoverage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalCoverageRepository extends SGXAuditableEntityJPARepository<MedicalCoverage, Integer> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT mc FROM MedicalCoverage mc WHERE upper(mc.name) = upper(:name)")
    List<MedicalCoverage> getByName(@Param("name") String name);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM medical_coverage mc WHERE mc.cuit = :cuit" ,  nativeQuery=true)
    Optional<MedicalCoverage> getMedicalCoverage(@Param("cuit") String cuit);

    @Transactional(readOnly = true)
    @Query(value = "SELECT exists (select 1 from medical_coverage where cuit = :cuit)", nativeQuery = true)
    boolean existsByCUIT(@Param("cuit") String cuit);

    @Transactional(readOnly = true)
    @Query(value = "SELECT exists (select 1 from medical_coverage where cuit = :cuit and id != :id)", nativeQuery = true)
    boolean existsByCUITandDiferentId(@Param("cuit") String cuit, @Param("id") Integer id);

    @Transactional(readOnly = true)
    @Query(value = "SELECT mc FROM MedicalCoverage mc WHERE mc.cuit = :cuit")
    Optional<MedicalCoverage> findByCUIT(@Param("cuit") String cuit);

	@Transactional(readOnly = true)
	@Query(value = "SELECT mc FROM MedicalCoverage mc WHERE mc.type = :type AND mc.deleteable.deleted = false")
	List<MedicalCoverage> findAllByType(Sort sort, @Param("type") Short type);

    @Transactional
    @Modifying
    @Query("DELETE FROM MedicalCoverage mc "+
            "WHERE mc.id = :id ")
    void deleteMergedCoverage(@Param("id") Integer id);
}
