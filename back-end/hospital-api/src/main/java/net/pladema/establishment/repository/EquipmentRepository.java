package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Equipment;

import net.pladema.establishment.repository.entity.Sector;

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

}

