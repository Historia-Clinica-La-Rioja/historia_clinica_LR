package net.pladema.snowstorm.repository;

import net.pladema.snowstorm.repository.entity.VSnomedGroupConcept;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface VSnomedGroupConceptRepository extends JpaRepository<VSnomedGroupConcept, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT vs " +
			"FROM VSnomedGroupConcept vs " +
			"WHERE vs.conceptId = :conceptId " +
			"AND vs.groupId = :groupId")
	Optional<VSnomedGroupConcept> getByConceptIdAndGroupId(@Param("conceptId") Integer conceptId, @Param("groupId") Integer groupId);
}
