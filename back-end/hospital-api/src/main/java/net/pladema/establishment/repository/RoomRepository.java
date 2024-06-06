package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT r FROM  Room r "
			+ " INNER JOIN Sector s ON s.id = r.sectorId "
			+ " INNER JOIN ClinicalSpecialtySector css ON css.sectorId = r.sectorId "
			+ " WHERE r.sectorId = :sectorId "
			+ " AND css.clinicalSpecialtyId = :specialtyId "
			+ " AND s.institutionId = :institutionId ")
	List<Room> getAllBySectorAndInstitution(@Param("sectorId") Integer sectorId,
			@Param("specialtyId") Integer specialtyId, @Param("institutionId") Integer institutionId);


	@Transactional(readOnly = true)
	@Query(value = " SELECT r FROM  Room AS r "
			+ " INNER JOIN Sector s ON s.id = r.sectorId "
			+ " WHERE s.institutionId = :institutionId ")
	List<Room> getAllByInstitution(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT s.institutionId " +
			"FROM  Room r " +
			"INNER JOIN Sector s ON (s.id = r.sectorId) " +
			"WHERE r.id = :id ")
    Integer getInstitutionId(@Param("id") Integer id);


	@Transactional(readOnly = true)
	@Query("SELECT r.id "+
			"FROM Room AS r ")
	List<Integer> getAllIds();

	@Transactional(readOnly = true)
	@Query(value = "SELECT r.id " +
			"FROM  Room r " +
			"INNER JOIN Sector s ON (s.id = r.sectorId) " +
			"WHERE s.institutionId IN :institutionsIds ")
	List<Integer> getAllIdsByInstitutionsId(@Param("institutionsIds") List<Integer> institutionsIds);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT r " +
			"FROM Room as r " +
			"WHERE r.roomNumber = :roomNumber " +
			"AND r.description = :description " +
			"AND r.sectorId = :sectorId ")
	Optional<Room> findBySectorIdAndRoomNumberAndDescription(@Param("sectorId") Integer sectorId,
															 @Param("description") String description,
															 @Param("roomNumber") String roomNumber);
	@Query(" SELECT r.roomNumber " +
			"FROM Room r " +
			"WHERE r.id = :roomId")
	String getRoomNumber(@Param("roomId") Integer roomId);

	@Transactional(readOnly = true)
	@Query(" SELECT r.sectorId " +
			"FROM Room r " +
			"WHERE r.id = :roomId")
	Integer getSectorId(@Param("roomId") Integer roomId);

}
