package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.RootSector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RootSectorRepository extends JpaRepository<RootSector, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT s.institutionId "+
			"FROM RootSector AS s " +
			"WHERE s.id = :id ")
	Integer getInstitutionId(@Param("id")  Integer id);

	@Transactional(readOnly = true)
	@Query("SELECT s.id "+
			"FROM RootSector AS s " +
			"WHERE s.institutionId IN :institutionsIds ")
	List<Integer> getAllIdsByInstitutionsId(@Param("institutionsIds") List<Integer> institutionsIds);
}
