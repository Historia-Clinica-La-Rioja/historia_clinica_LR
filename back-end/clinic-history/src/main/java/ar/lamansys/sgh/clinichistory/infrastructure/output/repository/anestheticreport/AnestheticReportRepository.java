package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anestheticreport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AnestheticReportRepository extends JpaRepository<AnestheticReport, Integer> {

	@Transactional(readOnly = true)
	Optional<AnestheticReport> findByDocumentId(Long documentId);
}
