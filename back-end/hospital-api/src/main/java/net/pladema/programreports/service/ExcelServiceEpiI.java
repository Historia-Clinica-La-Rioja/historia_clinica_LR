package net.pladema.programreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.programreports.repository.ConsultationDetailEpiI;

import java.util.List;

public interface ExcelServiceEpiI {

	IWorkbook buildExcelFromQuery(String tittle, String[] headers, List<ConsultationDetailEpiI> query);

}
