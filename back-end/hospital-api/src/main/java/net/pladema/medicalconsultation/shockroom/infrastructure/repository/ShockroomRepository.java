package net.pladema.medicalconsultation.shockroom.infrastructure.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.entity.Shockroom;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ShockroomRepository extends SGXAuditableEntityJPARepository<Shockroom, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo(" +
			"	s.id, " +
			"	s.description, " +
			"	(SELECT" +
			"		(CASE WHEN (COUNT(e.id) = 0) THEN true ELSE false END)" +
			"		FROM EmergencyCareEpisode e " +
			"		WHERE e.emergencyCareStateId = " + EmergencyCareState.EN_ATENCION +
			"		AND e.shockroomId = s.id " +
			"	)," +
			"	se.description, " +
			"	COALESCE(status.isBlocked, false)" +
			") " +
			"FROM Shockroom s " +
			"LEFT JOIN Sector se ON s.sectorId = se.id " +
			"LEFT JOIN AttentionPlaceStatus status ON status.id = s.statusId " +
			"WHERE s.institutionId = :institutionId AND s.deleteable.deleted = false "
	)
	List<ShockRoomBo> getShockroomsByInstitutionId(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(" SELECT s.description " +
			"FROM Shockroom s " +
			"WHERE s.id = :shockRoomId")
	String getDescription(@Param("shockRoomId") Integer shockRoomId);

	@Transactional(readOnly = true)
	@Query(" SELECT s.sectorId " +
			"FROM Shockroom s " +
			"WHERE s.id = :shockRoomId")
	Integer getSectorId(@Param("shockRoomId") Integer shockRoomId);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo(" +
	 		"	s.id, " +
	 		"	s.description, " +
			"	CASE WHEN (COUNT(e.id) = 0) THEN true ELSE false END, " +
			"	se.description, " +
			"	COALESCE(status.isBlocked, false)" +
			") " +
			"FROM Shockroom s " +
			"LEFT JOIN EmergencyCareEpisode e ON e.shockroomId = s.id AND e.emergencyCareStateId = :emergencyCareStateId AND e.deleteable.deleted = false " +
			"LEFT JOIN Sector se ON s.sectorId = se.id " +
			"LEFT JOIN AttentionPlaceStatus status ON status.id = s.statusId " +
			"WHERE s.sectorId = :sectorId AND s.deleteable.deleted = false " +
			"GROUP BY s.id, s.description, se.description, status.isBlocked")
	List<ShockRoomBo> findAllBySectorId(@Param("sectorId") Integer sectorId, @Param("emergencyCareStateId") Short emergencyCareStateId);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo(" +
	 		"	s.id, " +
	 		"	s.description, " +
			"	CASE WHEN (COUNT(e.id) = 0) THEN true ELSE false END, " +
			"	se.description, " +
			"	COALESCE(status.isBlocked, false)" +
			") " +
			"FROM Shockroom s " +
			"LEFT JOIN EmergencyCareEpisode e ON e.shockroomId = s.id AND e.emergencyCareStateId = :emergencyCareStateId AND e.deleteable.deleted = false " +
			"LEFT JOIN Sector se ON s.sectorId = se.id " +
			"LEFT JOIN AttentionPlaceStatus status ON status.id = s.statusId " +
			"WHERE s.id = :id " +
			"GROUP BY s.id, s.description, se.description, status.isBlocked")
	Optional<ShockRoomBo> findEmergencyCareShockRoomById(@Param("id") Integer id, @Param("emergencyCareStateId") Short emergencyCareStateId);

	@Modifying
	@Query("UPDATE Shockroom s SET s.statusId = :newStatusId WHERE s.id = :shockRoomId")
	void updateStatus(@Param("shockRoomId")Integer shockRoomId, @Param("newStatusId") Integer newStatusId);
}
