package net.pladema.clinichistory.documents.application;

import net.pladema.clinichistory.documents.domain.CHDocumentBo;
import net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo;
import net.pladema.clinichistory.documents.domain.HistoricClinicHistoryDownloadBo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClinicHistoryStorage {

	List<CHDocumentSummaryBo> getPatientClinicHistory(Integer patientId, LocalDate from, LocalDate to);

	List<CHDocumentBo> getClinicHistoryDocuments(List<Long> ids);

	Integer savePatientClinicHistoryLastPrint (Integer patientId, Integer userId, Integer institutionId);

	Optional<HistoricClinicHistoryDownloadBo> getPatientClinicHistoryLastDownload(Integer patientId, Integer institutionId);

}
