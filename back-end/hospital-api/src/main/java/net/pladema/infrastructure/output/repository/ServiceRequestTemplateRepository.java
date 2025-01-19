package net.pladema.infrastructure.output.repository;

import net.pladema.infrastructure.output.repository.entity.ServiceRequestTemplate;
import net.pladema.infrastructure.output.repository.entity.ServiceRequestTemplatePK;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ServiceRequestTemplateRepository extends JpaRepository<ServiceRequestTemplate, ServiceRequestTemplatePK> {

	@Transactional(readOnly = true)
	@Query("SELECT srt.pk.groupId, COUNT(srt) AS frequency " +
			"FROM ServiceRequestTemplate srt " +
			"JOIN ServiceRequest sr ON (sr.id = srt.pk.serviceRequestId) " +
			"WHERE sr.doctorId = :professionalId " +
			"AND sr.institutionId = :institutionId  " +
			"GROUP BY srt.pk.groupId " +
			"ORDER BY frequency DESC ")
	List<Integer> getMostFrequentTemplateIds(@Param("professionalId") Integer professionalId,
											 @Param("institutionId") Integer institutionId,
											 Pageable page);
}