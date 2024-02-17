package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.InstitutionReportReasonPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.InstitutionReportReason;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InstitutionReportReasonRepository extends JpaRepository<InstitutionReportReason, InstitutionReportReasonPK> {

	@Transactional(readOnly = true)
	@Query(" SELECT irr.pk.reasonId " +
			"FROM InstitutionReportReason irr " +
			"WHERE irr.pk.reportId = :reportId")
	List<Short> getByReportId(@Param("reportId") Integer reportId);

}
