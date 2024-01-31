package net.pladema.provincialreports.odontologicalreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.odontologicalreports.repository.OdontologicalProceduresConsultationDetail;
import net.pladema.provincialreports.odontologicalreports.repository.OdontologyConsultationDetail;

import java.util.List;

public interface OdontologicalReportExcelService {

	IWorkbook buildExcelOdontology(String title, String[] headers, List<OdontologyConsultationDetail> query, String startDate, String endDate, Integer institutionId);

	IWorkbook buildExcelOdontologicalProcedures(String title, String[] headers, List<OdontologicalProceduresConsultationDetail> query, String startDate, String endDate, String institutionName);

}
