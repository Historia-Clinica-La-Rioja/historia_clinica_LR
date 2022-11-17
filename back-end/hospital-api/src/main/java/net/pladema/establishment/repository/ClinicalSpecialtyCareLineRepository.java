package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.establishment.repository.entity.ClinicalSpecialtyCareLine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClinicalSpecialtyCareLineRepository extends SGXAuditableEntityJPARepository<ClinicalSpecialtyCareLine, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT cscl FROM ClinicalSpecialtyCareLine as cscl " +
            "WHERE cscl.careLineId = :careLineId " +
            "AND cscl.clinicalSpecialtyId = :clinicalSpecialtyId ")
    ClinicalSpecialtyCareLine findByCareLineIdAndClinicalSpecialtyId(@Param("careLineId") Integer careLineId, @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

    @Transactional(readOnly = true)
    @Query("SELECT cscl FROM ClinicalSpecialtyCareLine as cscl " +
            "WHERE cscl.careLineId = :careLineId " +
            "AND cscl.deleteable.deleted = false")
    List<ClinicalSpecialtyCareLine> findByCareLineId(@Param("careLineId") Integer careLineId);

    @Transactional(readOnly = true)
    @Query("SELECT cs FROM ClinicalSpecialty as cs " +
            "JOIN ClinicalSpecialtyCareLine as cscl " +
            "ON cs.id = cscl.clinicalSpecialtyId " +
            "WHERE cscl.careLineId = :careLineId " +
            "AND cscl.deleteable.deleted = false")
    List<ClinicalSpecialty> getAllByCareLineId(@Param("careLineId") Integer careLineId);
    
}