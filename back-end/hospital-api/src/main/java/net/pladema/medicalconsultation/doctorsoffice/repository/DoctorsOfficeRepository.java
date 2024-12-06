package net.pladema.medicalconsultation.doctorsoffice.repository;

import java.util.List;
import java.util.Optional;

import net.pladema.access.domain.enums.EClinicHistoryAccessReason;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

import org.springframework.data.jpa.repository.Modifying;
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
            "	do.id, do.description, do.openingTime, do.closingTime, " +
			"	COALESCE(status.isBlocked, false) " +
            ") " +
            "FROM DoctorsOffice do " +
			"LEFT JOIN AttentionPlaceStatus status ON status.id = do.statusId " +
            "WHERE do.institutionId = :institutionId " +
            "AND do.sectorId = :sectorId " +
			"AND do.deleteable.deleted IS FALSE " +
            "ORDER BY do.description ASC ")
    List<DoctorsOfficeVo> findAllBy(@Param("institutionId") Integer institutionId,
                                    @Param("sectorId") Integer sectorId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo(" +
            "	do.id, " +
            "	do.description, " +
            "	do.openingTime, " +
            "	do.closingTime, " +
			"	COALESCE(status.isBlocked, false) " +
            ") " +
            "FROM DoctorsOffice do " +
            "JOIN Sector s on (do.sectorId = s.id) " +
			"LEFT JOIN AttentionPlaceStatus status ON status.id = do.statusId " +
            "WHERE do.institutionId = :institutionId " +
            "AND s.sectorTypeId = :sectorTypeId " +
			"AND s.deleteable.deleted = false " +
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

    @Transactional(readOnly = true)
	@Query(" SELECT do.description " +
			"FROM DoctorsOffice do " +
			"WHERE do.id = :doctorsOfficeId")
	String getDescription(@Param("doctorsOfficeId") Integer doctorsOfficeId);

	@Transactional(readOnly = true)
	@Query(" SELECT do.sectorId " +
			"FROM DoctorsOffice do " +
			"WHERE do.id = :doctorsOfficeId")
	Integer getSectorId(@Param("doctorsOfficeId") Integer doctorsOfficeId);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo(" +
			"	d.id, " +
			"	d.description, " +
			"	d.openingTime, " +
			"	d.closingTime, " +
			"	CASE WHEN (COUNT(e.id) = 0) THEN true ELSE false END, "+
			"	s.description, "+
			"	COALESCE(status.isBlocked, false)" +
			") " +
			"FROM DoctorsOffice d " +
			"LEFT JOIN EmergencyCareEpisode e ON e.doctorsOfficeId = d.id AND e.emergencyCareStateId = :emergencyCareStateId AND e.deleteable.deleted = false " +
			"LEFT JOIN Sector s ON d.sectorId = s.id " +
			"LEFT JOIN AttentionPlaceStatus status ON status.id = d.statusId " +
			"WHERE d.sectorId = :sectorId AND d.deleteable.deleted = false " +
			"GROUP BY d.id, d.description, d.openingTime, d.closingTime, s.description, status.isBlocked")
	List<DoctorsOfficeBo> getAllBySectorId(@Param("sectorId") Integer sectorId, @Param("emergencyCareStateId") Short emergencyCareStateId);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo(" +
			"	d.id, " +
			"	d.description, " +
			"	d.openingTime, " +
			"	d.closingTime, " +
			"	CASE WHEN (COUNT(e.id) = 0) THEN true ELSE false END, " +
			"	s.description, " +
			"	COALESCE(status.isBlocked, false)" +
			") " +
			"FROM DoctorsOffice d " +
			"LEFT JOIN EmergencyCareEpisode e ON e.doctorsOfficeId = d.id AND e.emergencyCareStateId = :emergencyCareStateId AND e.deleteable.deleted = false " +
			"LEFT JOIN Sector s ON d.sectorId = s.id " +
			"LEFT JOIN AttentionPlaceStatus status ON status.id = d.statusId " +
			"WHERE d.id = :id " +
			"GROUP BY d.id, d.description, d.openingTime, d.closingTime, s.description, status.isBlocked")
	Optional<DoctorsOfficeBo> findEmergencyCareDoctorOfficeById(@Param("id") Integer id, @Param("emergencyCareStateId") Short emergencyCareStateId);

	@Modifying
	@Query("UPDATE DoctorsOffice d SET d.statusId = :newStatusId WHERE d.id = :doctorsOfficeId")
	void updateStatus(@Param("doctorsOfficeId") Integer doctorsOfficeId, @Param("newStatusId") Integer newStatusId);

	@Query(value = "SELECT do FROM DoctorsOffice do "
			+ " JOIN Sector s ON do.sectorId = s.id "
			+ " WHERE do.id = :doctorsOfficeId "
			+ " AND do.deleteable.deleted = false "
			+ " AND s.deleteable.deleted = false ")
	Optional<DoctorsOffice> findByIdAndInstitution(@Param("doctorsOfficeId") Integer doctorsOfficeId);
}
