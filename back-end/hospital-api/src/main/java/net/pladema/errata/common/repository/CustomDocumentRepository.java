package net.pladema.errata.common.repository;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomDocumentRepository extends JpaRepository<Document, Long> {

	@Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
			"FROM Document d " +
			"WHERE d.id = :id AND " +
			"(d.creationable.createdBy = :createdBy OR d.updateable.updatedBy = :updatedBy)")
	boolean existsByIdAndCreatedByOrUpdatedBy(@Param("id") Long id,
											  @Param("createdBy") Integer createdBy,
											  @Param("updatedBy") Integer updatedBy);
}
