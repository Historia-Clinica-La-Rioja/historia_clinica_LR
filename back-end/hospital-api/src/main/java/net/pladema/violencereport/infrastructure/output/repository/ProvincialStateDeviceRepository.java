package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.ProvincialStateDevicePK;
import net.pladema.violencereport.infrastructure.output.repository.entity.ProvincialStateDevice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProvincialStateDeviceRepository extends JpaRepository<ProvincialStateDevice, ProvincialStateDevicePK> {

	@Transactional(readOnly = true)
	@Query(" SELECT psd.pk.provincialStateDeviceId " +
			"FROM ProvincialStateDevice psd " +
			"WHERE psd.pk.reportId = :reportId")
	List<Short> getDeviceIdsByReportId(@Param("reportId") Integer reportId);

}
