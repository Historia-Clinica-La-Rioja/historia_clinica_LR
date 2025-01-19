package net.pladema.reports.imageNetworkProductivity.infrastructure.output;

import net.pladema.establishment.repository.entity.Institution;

import net.pladema.reports.imageNetworkProductivity.domain.InstitutionBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ImageNetworkInstitutionRepository extends JpaRepository<Institution, Integer> {

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.reports.imageNetworkProductivity.domain.InstitutionBo(i.name, d.description, i.sisaCode, p.description) " +
			"FROM Institution i " +
			"LEFT JOIN Address a ON (a.id = i.addressId) " +
			"LEFT JOIN City c ON (a.cityId = c.id) " +
			"LEFT JOIN Department d ON (c.departmentId = d.id) " +
			"LEFT JOIN Province p ON (d.provinceId = p.id) " +
			"WHERE i.id = :institutionId")
	InstitutionBo getImageNetworkProductivityReportInstitution(@Param("institutionId") Integer institutionId);
}
