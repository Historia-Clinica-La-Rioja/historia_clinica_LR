package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.HealthSystemOrganizationCoordinationPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.HealthSystemOrganizationCoordination;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HealthSystemOrganizationCoordinationRepository extends JpaRepository<HealthSystemOrganizationCoordination, HealthSystemOrganizationCoordinationPK> {

	@Transactional(readOnly = true)
	@Query(" SELECT hsoc.pk.healthSystemOrganizationId " +
			"FROM HealthSystemOrganizationCoordination hsoc " +
			"WHERE hsoc.pk.reportId = :reportId")
	List<Short> getOrganizationsByReportId(@Param("reportId") Integer reportId);

	@Transactional(readOnly = true)
	@Query(" SELECT hsoc.otherHealthSystemOrganization " +
			"FROM HealthSystemOrganizationCoordination hsoc " +
			"WHERE hsoc.pk.reportId = :reportId " +
			"AND hsoc.otherHealthSystemOrganization IS NOT NULL")
	String getOtherOrganizationByReportId(@Param("reportId") Integer reportId);

}
