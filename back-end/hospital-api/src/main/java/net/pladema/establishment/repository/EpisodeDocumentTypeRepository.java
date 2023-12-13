package net.pladema.establishment.repository;

import net.pladema.staff.repository.entity.EpisodeDocumentType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EpisodeDocumentTypeRepository extends JpaRepository<EpisodeDocumentType, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN COUNT(edt.id) > 0 THEN TRUE ELSE FALSE END) FROM EpisodeDocumentType as edt WHERE edt.consentId = :consentId")
	boolean existsConsentDocumentById(@Param("consentId") Integer consentId);
}
