package net.pladema.programreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.programreports.repository.ConsultationDetailEpiII;

import java.util.List;

public interface ExcelServiceEpidemiologiaII {

	IWorkbook buildExcelFromQuery(String tittle, String[] headers, List<ConsultationDetailEpiII> query);

}
