package net.pladema.generalreports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.generalreports.repository.ConsultationDetailDiabeticosHipertensos;
import net.pladema.generalreports.repository.ConsultationDetailEmergencias;

import java.util.List;

public interface ExcelServiceGR {

	IWorkbook buildExcelEmergencias(String tittle, String[] headers, List<ConsultationDetailEmergencias> query);

	IWorkbook buildExcelDiabeticosHipertensos(String tittle, String[] headers, List<ConsultationDetailDiabeticosHipertensos> query);

}
