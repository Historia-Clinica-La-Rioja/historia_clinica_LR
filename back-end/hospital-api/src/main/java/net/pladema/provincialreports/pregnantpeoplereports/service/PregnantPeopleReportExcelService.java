package net.pladema.provincialreports.pregnantpeoplereports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.pregnantpeoplereports.repository.PregnantAttentionsConsultationDetail;
import net.pladema.provincialreports.pregnantpeoplereports.repository.PregnantControlsConsultationDetail;

import java.util.List;

public interface PregnantPeopleReportExcelService {

	IWorkbook buildExcelPregnantAttentions(String title, String[] headers, List<PregnantAttentionsConsultationDetail> query, String startDate, String endDate, String institutionName, String observations);

	IWorkbook buildExcelPregnantControls(String title, String[] headers, List<PregnantControlsConsultationDetail> query, String startDate, String endDate, String institutionName, String observations);

}
