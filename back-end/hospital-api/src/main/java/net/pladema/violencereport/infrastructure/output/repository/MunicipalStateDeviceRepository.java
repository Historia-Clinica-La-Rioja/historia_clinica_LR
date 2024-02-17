package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.MunicipalStateDevicePK;
import net.pladema.violencereport.infrastructure.output.repository.entity.MunicipalStateDevice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MunicipalStateDeviceRepository extends JpaRepository<MunicipalStateDevice, MunicipalStateDevicePK> {

	@Transactional(readOnly = true)
	@Query(" SELECT msd.pk.municipalStateDeviceId " +
			"FROM MunicipalStateDevice msd " +
			"WHERE msd.pk.reportId = :reportId")
	List<Short> getDeviceIdsByReportId(@Param("reportId") Integer reportId);

}
