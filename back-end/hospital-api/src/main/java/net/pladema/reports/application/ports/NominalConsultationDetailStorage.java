package net.pladema.reports.application.ports;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.reports.repository.ConsultationDetail;

import java.util.List;

public interface NominalConsultationDetailStorage {

	IWorkbook buildNominalExternalConsultationDetailReport(String title, String[] headers, List<ConsultationDetail> result, Integer institutionId);

}
