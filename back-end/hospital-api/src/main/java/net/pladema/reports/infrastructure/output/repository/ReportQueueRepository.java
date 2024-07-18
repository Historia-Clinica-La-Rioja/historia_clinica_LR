package net.pladema.reports.infrastructure.output.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.reports.infrastructure.output.repository.entity.ReportQueue;

@Repository
public interface ReportQueueRepository extends JpaRepository<ReportQueue, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT rq FROM ReportQueue rq " +
			" WHERE rq.generatedOn IS NULL " +
			" ORDER BY rq.createdOn")
	Page<ReportQueue> findToProcessByAge(Pageable pageable);
}
