package snomed.relations.cache.infrastructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import snomed.relations.cache.infrastructure.output.repository.entity.VMedicationPresentationUnit;
import org.springframework.transaction.annotation.Transactional;


import java.util.stream.Stream;

@Repository
public interface VMedicationPresentationUnitRepository extends JpaRepository<VMedicationPresentationUnit, String> {

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT vmpu.presentationUnitQuantity " +
			"FROM VMedicationPresentationUnit vmpu " +
			"WHERE vmpu.sctid = :sctid")
	Stream<String> fetchMedicationPresentationUnits(@Param("sctid") String medicationSctid);

}
