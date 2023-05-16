package net.pladema.clinichistory.documents.infrastructure.output.repository;

import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.HistoricClinicHistoryDownload;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HistoricClinicHistoryDownloadRepository extends JpaRepository<HistoricClinicHistoryDownload, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT hchd " +
			"FROM HistoricClinicHistoryDownload hchd " +
			"WHERE hchd.patientId = :patientId " +
			"AND hchd.institutionId = :institutionId " +
			"ORDER BY hchd.downloadDate DESC ")
	List<HistoricClinicHistoryDownload> getPatientClinicHistoryHistoricDownloads(@Param("patientId") Integer patientId, @Param("institutionId") Integer institutionId);

}