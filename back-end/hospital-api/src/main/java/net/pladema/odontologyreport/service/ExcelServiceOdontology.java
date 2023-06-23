package net.pladema.odontologyreport.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.odontologyreport.repository.ConsultationDetailOdontology;
import net.pladema.odontologyreport.repository.OdontologyProceduresReports;

import java.util.List;

public interface ExcelServiceOdontology {

	IWorkbook buildExcelOdontology(String tittle, String[] headers, List<ConsultationDetailOdontology> query);

	IWorkbook buildExcelOdontologyProcedures(String tittle, String[] headers, List<OdontologyProceduresReports> query);

}
