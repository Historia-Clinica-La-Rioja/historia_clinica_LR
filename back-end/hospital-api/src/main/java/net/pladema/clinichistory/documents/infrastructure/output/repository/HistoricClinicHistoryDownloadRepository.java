package net.pladema.clinichistory.documents.infrastructure.output.repository;

import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.HistoricClinicHistoryDownload;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricClinicHistoryDownloadRepository extends JpaRepository<HistoricClinicHistoryDownload, Integer> {
}