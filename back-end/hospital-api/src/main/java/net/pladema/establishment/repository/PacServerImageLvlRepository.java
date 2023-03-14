package net.pladema.establishment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.establishment.repository.entity.PacServerImageLvl;

public interface PacServerImageLvlRepository extends JpaRepository<PacServerImageLvl, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT COUNT(psil.id) FROM PacServerImageLvl psil WHERE psil.sectorId = :sectorId")
	Integer countElementsBySector(@Param("sectorId") Integer sectorId);
}
