package net.pladema.odontologicalreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.odontologicalreports.repository.OdontologicalProceduresConsultationDetail;
import net.pladema.odontologicalreports.repository.OdontologyConsultationDetail;

import java.util.List;

public interface OdontologicalReportExcelService {

	IWorkbook buildExcelOdontology(String title, String[] headers, List<OdontologyConsultationDetail> query);

	IWorkbook buildExcelOdontologicalProcedures(String title, String[] headers, List<OdontologicalProceduresConsultationDetail> query, String startDate, String endDate, String institutionName);

}
