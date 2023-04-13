package net.pladema.clinichistory.documents.application;

import net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo;

import java.time.LocalDate;
import java.util.List;

public interface ClinicHistoryStorage {

	List<CHDocumentSummaryBo> getPatientClinicHistory(Integer patientId, LocalDate from, LocalDate to);

}
