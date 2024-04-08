package net.pladema.medicalconsultation.shockroom.infrastructure.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.entity.Shockroom;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ShockroomRepository extends SGXAuditableEntityJPARepository<Shockroom, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT s " +
			"FROM Shockroom s " +
			"WHERE s.institutionId = :institutionId " +
			"AND s.deleteable.deleted = false")
	List<Shockroom> getShockrooms(@Param("institutionId") Integer institutionId);

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
}
