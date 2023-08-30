package net.pladema.olderadultsreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.olderadultsreports.repository.OlderAdultsHospitalizationConsultationDetail;
import net.pladema.olderadultsreports.repository.OlderAdultsOutpatientConsultationDetail;
import net.pladema.olderadultsreports.repository.PolypharmacyConsultationDetail;

import java.util.List;

public interface OlderAdultsReportExcelService {

	IWorkbook buildExcelOlderAdultsOutpatient(String title, String[] headers, List<OlderAdultsOutpatientConsultationDetail> query, String startDate, String endDate);

	IWorkbook buildExcelOlderAdultsHospitalization(String title, String[] headers, List<OlderAdultsHospitalizationConsultationDetail> query, String startDate, String endDate);

	IWorkbook buildExcelPolypharmacy(String title, String[] headers, List<PolypharmacyConsultationDetail> query, String startDate, String endDate, String institutionName);

}
