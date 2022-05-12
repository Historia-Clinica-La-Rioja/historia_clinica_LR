package net.pladema.programreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.programreports.repository.ConsultationDetailRecupero;

import java.util.List;

public interface ExcelServiceRecupero {

	IWorkbook buildExcelFromQuery(String tittle, String[] headers, List<ConsultationDetailRecupero> query);

}
