package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.InstitutionReportPlacePK;
import net.pladema.violencereport.infrastructure.output.repository.entity.InstitutionReportPlace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InstitutionReportPlaceRepository extends JpaRepository<InstitutionReportPlace, InstitutionReportPlacePK> {

	@Transactional(readOnly = true)
	@Query(" SELECT irp.pk.reportPlaceId " +
			"FROM InstitutionReportPlace irp " +
			"WHERE irp.pk.reportId = :reportId")
	List<Short> getIdsByReportId(@Param("reportId") Integer reportId);

	@Transactional(readOnly = true)
	@Query(" SELECT irp.otherReportPlace " +
			"FROM InstitutionReportPlace irp " +
			"WHERE irp.pk.reportId = :reportId " +
			"AND irp.otherReportPlace IS NOT NULL")
	String getOtherPlaceByReportId(@Param("reportId") Integer reportId);
}
