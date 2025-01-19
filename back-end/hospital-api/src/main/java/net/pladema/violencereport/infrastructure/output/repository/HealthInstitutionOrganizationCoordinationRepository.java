package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.HealthInstitutionOrganizationCoordinationPK;
import net.pladema.violencereport.infrastructure.output.repository.entity.HealthInstitutionOrganizationCoordination;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HealthInstitutionOrganizationCoordinationRepository extends JpaRepository<HealthInstitutionOrganizationCoordination, HealthInstitutionOrganizationCoordinationPK> {

	@Transactional(readOnly = true)
	@Query(" SELECT hioc.pk.healthInstitutionOrganizationId " +
			"FROM HealthInstitutionOrganizationCoordination hioc " +
			"WHERE hioc.pk.reportId = :reportId")
	List<Short> getOrganizationsByReportId(@Param("reportId") Integer reportId);

	@Transactional(readOnly = true)
	@Query(" SELECT hioc.otherHealthInstitutionOrganization " +
			"FROM HealthInstitutionOrganizationCoordination hioc " +
			"WHERE hioc.pk.reportId = :reportId " +
			"AND hioc.otherHealthInstitutionOrganization IS NOT NULL")
	String getOtherOrganizationByReportId(@Param("reportId") Integer reportId);

}
