package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.SexualViolenceActionPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.SexualViolenceAction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SexualViolenceActionRepository extends JpaRepository<SexualViolenceAction, SexualViolenceActionPK> {

	@Transactional(readOnly = true)
	@Query(" SELECT sva.pk.actionId " +
			"FROM SexualViolenceAction sva " +
			"WHERE sva.pk.reportId = :reportId")
	List<Short> getByReportId(@Param("reportId") Integer reportId);

}
