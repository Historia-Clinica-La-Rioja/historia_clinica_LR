package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Equipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT e "+
			"FROM Equipment AS e " +
			"WHERE e.sectorId = :sectorId ")
	List<Equipment> getEquipmentBySector(@Param("sectorId") Integer sectorId);

	@Transactional(readOnly = true)
	@Query("SELECT e " +
			"FROM Equipment AS e " +
			"INNER JOIN Sector AS s ON s.id = e.sectorId " +
			"INNER JOIN Institution i ON i.id = s.institutionId " +
			"WHERE i.id = :institutionId ")
	List<Equipment> getEquipmentByInstitution(@Param("institutionId") Integer sectorId);

	@Transactional(readOnly = true)
	@Query("SELECT e "+
			"FROM Equipment AS e " +
			"JOIN Sector AS s ON (s.id = e.sectorId)" +
			"WHERE e.modalityId = :modalityId AND s.institutionId = :institutionId")
	List<Equipment> getEquipmentByModalityInInstitution(@Param("modalityId") Integer modalityId,
														@Param("institutionId") Integer institutionId);

}

