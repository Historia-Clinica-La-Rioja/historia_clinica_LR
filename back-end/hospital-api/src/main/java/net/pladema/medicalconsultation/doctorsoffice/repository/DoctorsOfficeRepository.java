package net.pladema.medicalconsultation.doctorsoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;

@Repository
public interface DoctorsOfficeRepository extends SGXAuditableEntityJPARepository<DoctorsOffice, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo(" +
            "do.id, do.description, do.openingTime, do.closingTime) " +
            "FROM DoctorsOffice do " +
            "WHERE do.institutionId = :institutionId " +
            "AND do.sectorId = :sectorId " +
			"AND do.deleteable.deleted IS FALSE " +
            "ORDER BY do.description ASC ")
    List<DoctorsOfficeVo> findAllBy(@Param("institutionId") Integer institutionId,
                                    @Param("sectorId") Integer sectorId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo(" +
            "do.id, do.description, do.openingTime, do.closingTime) " +
            "FROM DoctorsOffice do " +
            "JOIN Sector s on (do.sectorId = s.id) " +
            "WHERE do.institutionId = :institutionId " +
            "AND s.sectorTypeId = :sectorTypeId " +
			"AND do.deleteable.deleted IS FALSE " +
            "ORDER BY do.description ASC ")
    List<DoctorsOfficeVo> findAllBySectorType(@Param("institutionId") Integer institutionId,
                                    @Param("sectorTypeId") Short sectorTypeId);

    @Transactional(readOnly = true)
    @Query("SELECT d.institutionId "+
            "FROM DoctorsOffice AS d " +
            "WHERE d.id = :id ")
    Integer getInstitutionId(@Param("id") Integer id);

    @Transactional(readOnly = true)
    @Query("SELECT d.id "+
            "FROM DoctorsOffice AS d " +
            "WHERE d.institutionId IN :institutionsIds " +
			"AND d.deleteable.deleted IS FALSE ")
    List<Integer> getAllIdsByInstitutionsId(@Param("institutionsIds") List<Integer> institutionsIds);

	@Transactional(readOnly = true)
	@Query("SELECT d " +
			"FROM DoctorsOffice d " +
			"WHERE d.institutionId = :institutionId " +
			"AND d.sectorId = :sectorId " +
			"AND d.description = :description " +
			"AND d.deleteable.deleted IS FALSE")
	Optional<DoctorsOffice> findByInstitutionIdAndDescription(@Param("institutionId") Integer institutionId,
															  @Param("description") String description,
															  @Param("sectorId") Integer sectorId);
}
